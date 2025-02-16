package com.ssukssugi.ssukssugilji.common.webclient;

import com.ssukssugi.ssukssugilji.user.dto.apple.AppleUserInfoResponse;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${apple.client.secret}")
    private String APPLE_CLIENT_SECRET;

    // https://developer.apple.com/documentation/signinwithapplerestapi/generate-and-validate-tokens
    private static final String GET_USERINFO_API_URL = "https://appleid.apple.com/auth/token";

    @PostConstruct
    private void init() {
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
    }

    @Override
    public AppleUserInfoResponse getUserInfo(String accessToken) {
        ResponseEntity<Map> response = webClient
            .post()
            .uri(uriBuilder -> uriBuilder.path(GET_USERINFO_API_URL)
                .build())
            .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
            .bodyValue("client_id=" + APPLE_CLIENT_ID +
                "&client_secret=" + APPLE_CLIENT_SECRET +
                "&code=" + accessToken +
                "&grant_type=authorization_code" +
                "&redirect_uri=https://your.redirect.uri")
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

    private AppleUserInfoResponse parseMap(Map<String, String> map) {
        AppleUserInfoResponse response = new AppleUserInfoResponse();
        response.setAccessToken(map.get("access_token"));
        response.setTokenType(map.get("token_type"));
        response.setExpiresIn(Integer.valueOf(map.get("expires_in")));
        response.setRefreshToken(map.get("refresh_token"));
        response.setIdToken(map.get("id_token"));
        return response;
    }
}
