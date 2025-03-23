package com.ssukssugi.ssukssugilji.common.webclient;

import com.ssukssugi.ssukssugilji.user.dto.apple.AppleUserInfoResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import jakarta.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Component
@Slf4j
public class AppleWebClientProxyImpl implements WebClientProxy {

    private WebClient webClient;

    @Value("${apple.client.id}")
    private String APPLE_CLIENT_ID;

    @Value("${apple.key.id}")
    private String APPLE_KEY_ID;

    @Value("${apple.team.id}")
    private String APPLE_TEAM_ID;

    @Value("${apple.private.key.file.path}")
    private Resource PRIVATE_KEY_PATH;

    private String APPLE_CLIENT_SECRET;

    // https://developer.apple.com/documentation/signinwithapplerestapi/generate-and-validate-tokens
    private static final String GET_USERINFO_API_URL = "https://appleid.apple.com/auth/token";

    @PostConstruct
    private void init() throws Exception {
        HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .responseTimeout(Duration.ofMillis(5000))
            .doOnConnected(conn ->
                conn.addHandlerLast(new ReadTimeoutHandler(5000))
                    .addHandlerLast(new WriteTimeoutHandler(5000)));

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

    @Override
    public AppleUserInfoResponse getUserInfo(String accessToken) {
        ResponseEntity<Map> response = webClient
            .post()
            .uri(GET_USERINFO_API_URL)
            .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
            .bodyValue("client_id=" + APPLE_CLIENT_ID +
                "&client_secret=" + APPLE_CLIENT_SECRET +
                "&code=" + accessToken +
                "&grant_type=authorization_code")
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, error -> {
                log.error("Apple API 4xx Error: {}", error.statusCode());
                return error.createException();
            })
            .onStatus(HttpStatusCode::is5xxServerError, error -> {
                log.error("Apple API 5xx Error: {}", error.statusCode());
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
        AppleUserInfoResponse response = new AppleUserInfoResponse();
        response.setAccessToken((String) map.get("access_token"));
        response.setTokenType((String) map.get("token_type"));
        response.setExpiresIn((Integer) map.get("expires_in"));
        response.setRefreshToken((String) map.get("refresh_token"));
        response.setIdToken((String) map.get("id_token"));
        return response;
    }

    private PrivateKey getPrivateKey() throws Exception {
        byte[] keyBytes = Files.readAllBytes(
            Paths.get(PRIVATE_KEY_PATH.getFile().getAbsolutePath()));
        String privateKeyPEM = new String(keyBytes)
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replaceAll("\\s", "");

        byte[] decoded = java.util.Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePrivate(spec);
    }
}
