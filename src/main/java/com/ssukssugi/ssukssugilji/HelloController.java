package com.ssukssugi.ssukssugilji;

import com.ssukssugi.ssukssugilji.common.CloudflareR2Service;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class HelloController {

    private final CloudflareR2Service cloudflareR2Service;

    @GetMapping("/hello")
    public String hello() {
        return "Hello, this is ssukssugilji server!";
    }

    @PostMapping("/image")
    public ResponseEntity<String> testUploadImage(@RequestParam("file") MultipartFile file,
        @RequestParam String fileName)
        throws IOException {
        return ResponseEntity.ok(cloudflareR2Service.uploadFile(fileName, file));
    }
}
