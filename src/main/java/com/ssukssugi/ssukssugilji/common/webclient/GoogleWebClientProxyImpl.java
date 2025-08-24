package com.ssukssugi.ssukssugilji.common.webclient;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.ssukssugi.ssukssugilji.user.dto.google.GoogleUserInfoResponse;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import jakarta.annotation.PostConstruct;
import jakarta.security.auth.message.AuthException;
import java.time.Duration;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Component
@Slf4j
public class GoogleWebClientProxyImpl implements WebClientProxy {

    private WebClient webClient;

    private static final String CLIENT_ID = "341233184627-s07ukai2jbjbm6khdbnfgip5e793la72.apps.googleusercontent.com";

    @Value("${google.api.key}")
    private String GOOGLE_API_KEY;

    // https://cloud.google.com/identity-platform/docs/use-rest-api?hl=ko#section-get-account-info
    private static final String GET_USERINFO_API_URL = "https://identitytoolkit.googleapis.com/v1/accounts:lookup";

    @PostConstruct
    private void init() {
        HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .responseTimeout(Duration.ofMillis(5000))
            .doOnConnected(conn ->
                conn.addHandlerLast(new ReadTimeoutHandler(5000))
                    .addHandlerLast(new WriteTimeoutHandler(5000)));

        webClient = WebClient.builder()
            .baseUrl("https://identitytoolkit.googleapis.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }

    public GoogleIdToken.Payload verifyToken(String idTokenString) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
            new NetHttpTransport(),
            new GsonFactory()
        )
            .setAudience(Collections.singletonList(CLIENT_ID)) // 내 앱의 client_id 검증
            .build();

        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                return idToken.getPayload(); // 유효 → 클레임 추출 가능
            } else {
                throw new RuntimeException("idToken is null");
            }
        } catch (Exception e) {
            log.error("Google ID Token verification failed: {}", e.getMessage());
            throw new RuntimeException("Invalid ID Token ", e);
        }
    }

    @Data
    @AllArgsConstructor
    private static class GoogleAuthRequest {

        private String idToken;
    }

    @Override
    public GoogleUserInfoResponse getUserInfo(String accessToken) {
        verifyToken(accessToken);
        ResponseEntity<GoogleUserInfoResponse> response = webClient
            .post()
            .uri(uriBuilder -> uriBuilder.path("/v1/accounts:lookup")
                .queryParam("key", GOOGLE_API_KEY)
                .build())
            .bodyValue(new GoogleAuthRequest(accessToken))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, error -> error.bodyToMono(String.class)
                .flatMap(errorBody -> {
                    log.error("Google API {} Error: {}, Body: {}",
                        error.statusCode(), error, errorBody);
                    return Mono.error(new AuthException("Google Auth Failed: " + errorBody));
                }))
            .onStatus(HttpStatusCode::is5xxServerError, error -> {
                log.error("Google API {} Error: {}", error.statusCode(), error);
                return error.createException();
            })
            .toEntity(GoogleUserInfoResponse.class)
            .block();

        if (response == null) {
            throw new RuntimeException("Google API response is null");
        }
        if (response.getBody() == null) {
            throw new RuntimeException("Google API response body is null");
        }

        return response.getBody();
    }
}
