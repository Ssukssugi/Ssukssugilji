package com.ssukssugi.ssukssugilji.common.webclient;

import com.ssukssugi.ssukssugilji.user.dto.google.GoogleUserInfoResponse;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
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
public class GoogleWebClientProxyImpl implements WebClientProxy {

    private WebClient webClient;

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
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }

    @Override
    public GoogleUserInfoResponse getUserInfo(String accessToken) {
        ResponseEntity<GoogleUserInfoResponse> response = webClient
            .post()
            .uri(uriBuilder -> uriBuilder.path(GET_USERINFO_API_URL)
                .queryParam("key", GOOGLE_API_KEY)
                .build())
            .bodyValue("{\"idToken\": \"" + accessToken + "\"}")
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, error -> {
                log.error("Google API 4xx Error: {}", error.statusCode());
                return error.createException();
            })
            .onStatus(HttpStatusCode::is5xxServerError, error -> {
                log.error("Google API 5xx Error: {}", error.statusCode());
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
