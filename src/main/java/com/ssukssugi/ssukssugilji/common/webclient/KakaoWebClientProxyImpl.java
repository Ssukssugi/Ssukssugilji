package com.ssukssugi.ssukssugilji.common.webclient;

import com.ssukssugi.ssukssugilji.user.dto.kakao.KakaoUserInfoResponse;
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
public class KakaoWebClientProxyImpl implements WebClientProxy {

    private WebClient webClient;

    // https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info
    private static final String GET_USERINFO_API_URL = "https://kapi.kakao.com/v2/user/me";

    @PostConstruct
    private void init() {
        HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .responseTimeout(Duration.ofMillis(5000))
            .doOnConnected(conn ->
                conn.addHandlerLast(new ReadTimeoutHandler(5000))
                    .addHandlerLast(new WriteTimeoutHandler(5000)));

        webClient = WebClient.builder()
            .defaultHeader(HttpHeaders.CONTENT_TYPE,
                MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8")
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }

    @Override
    public KakaoUserInfoResponse getUserInfo(String accessToken) {
        ResponseEntity<KakaoUserInfoResponse> response = webClient
            .get()
            .uri(GET_USERINFO_API_URL)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, error -> {
                log.error("Kakao API 4xx Error: {}", error.statusCode());
                return error.createException();
            })
            .onStatus(HttpStatusCode::is5xxServerError, error -> {
                log.error("Kakao API 5xx Error: {}", error.statusCode());
                return error.createException();
            })
            .toEntity(KakaoUserInfoResponse.class)
            .block();

        if (response == null) {
            throw new RuntimeException("Kakao API response is null");
        }
        if (response.getBody() == null) {
            throw new RuntimeException("Kakao API response body is null");
        }

        return response.getBody();
    }
}
