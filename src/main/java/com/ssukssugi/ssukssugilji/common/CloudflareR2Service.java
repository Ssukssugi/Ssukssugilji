package com.ssukssugi.ssukssugilji.common;

import com.ssukssugi.ssukssugilji.common.error.exception.InvalidRequestException;
import jakarta.annotation.PostConstruct;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudflareR2Service {

    private S3Client s3Client;

    @Value("${cloudflare.r2.bucket}")
    private String bucketName;
    @Value("${cloudflare.r2.endpoint}")
    private String endpoint;
    @Value("${cloudflare.r2.access-key}")
    private String accessKey;
    @Value("${cloudflare.r2.secret-key}")
    private String secretKey;

    @PostConstruct
    private void initS3Client() {
        this.s3Client = S3Client.builder()
            .endpointOverride(URI.create(endpoint))
            .credentialsProvider(
                StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
            .region(Region.US_EAST_1) // Cloudflare R2는 리전이 필요 없지만 AWS SDK 특성상 설정 필요
            .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
            .build();
    }

    public String uploadFile(String fileName, MultipartFile file) throws IOException {
        validateFileName(fileName);
        BufferedImage image = ImageIO.read(file.getInputStream());
        float quality = calculateQuality(file, image);
        byte[] webpBytes = convertToWebPBytes(image, quality);
        return uploadToR2(fileName, webpBytes);
    }

    private float calculateQuality(MultipartFile file, BufferedImage image) {
        long fileSizeMB = file.getSize() / (1024 * 1024);
        float quality;
        if (fileSizeMB > 10) {
            quality = 0.75f;
        } else if (fileSizeMB > 3) {
            quality = 0.85f;
        } else {
            quality = 0.9f;
        }
        if (image.getWidth() > 3000 || image.getHeight() > 3000) {
            quality = Math.min(quality, 0.8f);
        }
        return quality;
    }

    private byte[] convertToWebPBytes(BufferedImage image, float quality) throws IOException {
        ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();
        try {
            ImageWriteParam writeParam = writer.getDefaultWriteParam();
            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            writeParam.setCompressionQuality(quality);
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
                writer.setOutput(ios);
                writer.write(null, new IIOImage(image, null, null), writeParam);
                ios.flush();
                return baos.toByteArray();
            }
        } finally {
            writer.dispose();
        }
    }

    private String uploadToR2(String fileName, byte[] webpBytes) throws IOException {
        try (InputStream webpInputStream = new ByteArrayInputStream(webpBytes)) {
            PutObjectResponse response = s3Client.putObject(
                PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName.substring(1))
                    .contentType("image/webp")
                    .build(),
                RequestBody.fromInputStream(webpInputStream, webpBytes.length)
            );
            if (response.sdkHttpResponse().isSuccessful()) {
                return fileName;
            } else {
                throw new InvalidRequestException(
                    "Cloudflare R2 upload failed: " +
                        response.sdkHttpResponse().statusText().orElse("Unknown error")
                );
            }
        }
    }

    private void validateFileName(String fileName) {
        if (!fileName.startsWith("/")) {
            throw new InvalidRequestException(
                "File name must start with a slash. Provided: " + fileName);
        }

        if (!fileName.endsWith(".webp")) {
            throw new InvalidRequestException(
                "File name must end with .webp extension. Provided: " + fileName);
        }
    }
}