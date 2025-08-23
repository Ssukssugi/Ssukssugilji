package com.ssukssugi.ssukssugilji.common.webclient;

import com.ssukssugi.ssukssugilji.user.dto.apple.AppleUserInfoResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import jakarta.annotation.PostConstruct;
import jakarta.security.auth.message.AuthException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Component
@Slf4j
@RequiredArgsConstructor
public class AppleWebClientProxyImpl implements WebClientProxy {

    private WebClient webClient;

    @Value("${apple.client.id}")
    private String APPLE_CLIENT_ID;

    @Value("${apple.key.id}")
    private String APPLE_KEY_ID;

    @Value("${apple.team.id}")
    private String APPLE_TEAM_ID;

    @Value("${apple.private.key.file.path}")
    private String PRIVATE_KEY_PATH;

    private String APPLE_CLIENT_SECRET;
    private final ResourceLoader resourceLoader;

    // https://developer.apple.com/documentation/signinwithapplerestapi/generate-and-validate-tokens
    private static final String GET_USERINFO_API_URL = "https://appleid.apple.com/auth/token";

    @PostConstruct
    private void init() throws Exception {
        HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .responseTimeout(Duration.ofSeconds(5))
            .doOnConnected(conn ->
                conn.addHandlerLast(new ReadTimeoutHandler(5, TimeUnit.SECONDS))
                    .addHandlerLast(new WriteTimeoutHandler(5, TimeUnit.SECONDS)));

        webClient = WebClient.builder()
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();

        APPLE_CLIENT_SECRET = generateClientSecret();
    }

    private String generateClientSecret() throws Exception {
        PrivateKey privateKey = getPrivateKey();

        long now = System.currentTimeMillis();
        long expiresIn = 1000L * 60 * 60 * 24 * 180; // 최대 6개월 (180일)

        return Jwts.builder()
            .setHeaderParam("alg", "ES256")
            .setHeaderParam("kid", APPLE_KEY_ID)
            .setIssuer(APPLE_TEAM_ID)
            .setIssuedAt(new Date(now))
            .setExpiration(new Date(now + expiresIn))
            .setAudience("https://appleid.apple.com")
            .setSubject(APPLE_CLIENT_ID)
            .signWith(privateKey, SignatureAlgorithm.ES256)
            .compact();
    }

    private PrivateKey getPrivateKey() throws Exception {
        Resource privateKeyFile = resourceLoader.getResource(PRIVATE_KEY_PATH);
        byte[] keyBytes = privateKeyFile.getInputStream().readAllBytes();
        String privateKeyPEM = new String(keyBytes)
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replaceAll("\\s", "");

        byte[] decoded = java.util.Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePrivate(spec);
    }

    @Override
    public AppleUserInfoResponse getUserInfo(String accessToken) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", APPLE_CLIENT_ID);
        params.add("client_secret", APPLE_CLIENT_SECRET);
        params.add("code", accessToken);
        params.add("grant_type", "authorization_code");

        ResponseEntity<Map> response = webClient
            .post()
            .uri(GET_USERINFO_API_URL)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue(params)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, error -> error.bodyToMono(String.class)
                .flatMap(errorBody -> {
                    log.error("Apple API {} Error: {}, Body: {}",
                        error.statusCode(), error, errorBody);
                    return Mono.error(new AuthException("Apple Auth Failed: " + errorBody));
                }))
            .onStatus(HttpStatusCode::is5xxServerError, error -> {
                log.error("Apple API {} Error: {}", error.statusCode(), error);
                return error.createException();
            })
            .toEntity(Map.class)
            .block();

        if (response == null) {
            throw new RuntimeException("Apple API response is null");
        }
        if (response.getBody() == null) {
            throw new RuntimeException("Apple API response body is null");
        }

        if (response.getStatusCode() != HttpStatusCode.valueOf(200)) {
            throw new RuntimeException("");
        }

        return parseMap(response.getBody());
    }

    private AppleUserInfoResponse parseMap(Map<String, Object> map) {
        try {
            return AppleUserInfoResponse.builder()
                .accessToken(Objects.toString(map.get("access_token"), null))
                .tokenType(Objects.toString(map.get("token_type"), null))
                .expiresIn(map.get("expires_in") instanceof Integer ?
                    (Integer) map.get("expires_in") : null)
                .refreshToken(Objects.toString(map.get("refresh_token"), null))
                .idToken(Objects.toString(map.get("id_token"), null))
                .build();
        } catch (Exception e) {
            log.error("Failed to parse Apple response: {}", map, e);
            throw new RuntimeException("Invalid response format from Apple");
        }
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // Daily
    private void refreshClientSecretDaily() {
        try {
            APPLE_CLIENT_SECRET = generateClientSecret();
            log.info("Apple client secret refreshed successfully");
        } catch (Exception e) {
            log.error("Failed to refresh Apple client secret", e);
        }
    }
}
