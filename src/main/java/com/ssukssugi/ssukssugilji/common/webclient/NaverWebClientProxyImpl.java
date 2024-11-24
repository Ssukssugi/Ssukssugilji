package com.ssukssugi.ssukssugilji.common.webclient;

import com.ssukssugi.ssukssugilji.user.dto.naver.NaverUserInfoResponse;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
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
public class NaverWebClientProxyImpl implements WebClientProxy {

    private WebClient webClient;

    // https://developers.naver.com/docs/login/profile/profile.md
    private static final String GET_USERINFO_API_URL = "https://openapi.naver.com/v1/nid/me";

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
    public NaverUserInfoResponse getUserInfo(String accessToken) {
        ResponseEntity<NaverUserInfoResponse> response = webClient
            .get()
            .uri(GET_USERINFO_API_URL)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, error -> {
                log.error("Google API 4xx Error: {}", error.statusCode());
                return error.createException();
            })
            .onStatus(HttpStatusCode::is5xxServerError, error -> {
                log.error("Google API 5xx Error: {}", error.statusCode());
                return error.createException();
            })
            .toEntity(NaverUserInfoResponse.class)
            .block();

        if (response == null) {
            throw new RuntimeException("Naver API response is null");
        }
        if (response.getBody() == null) {
            throw new RuntimeException("Naver API response body is null");
        }

        return response.getBody();
    }
}
