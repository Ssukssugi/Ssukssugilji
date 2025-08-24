package com.ssukssugi.ssukssugilji.common.configuration;

import com.ssukssugi.ssukssugilji.auth.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    public static final String PERMITTED_URI[] = {"/api/v1/auth/**", "/hello"};
    private static final String PERMITTED_ROLES[] = {"USER"};

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
//            .cors(corsCustomizer -> corsCustomizer
//                .configurationSource(customCorsConfigurationSource)
//            )
            .csrf(AbstractHttpConfigurer::disable)
//            .httpBasic(HttpBasicConfigurer::disable)
            // OAuth 사용으로 인한 form login 비활성화
//            .formLogin(FormLoginConfigurer::disable)
            .authorizeHttpRequests(request -> request
                    // 특정 권한이 있어야만 특정 API에 접근할 수 있도록 설정
//                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                    // 특정 API들은 별도의 인증/인가 과정 없이도 접근이 가능하도록 설정
                    .requestMatchers(PERMITTED_URI).permitAll()
                    // 그 외의 요청들은 PERMITTED_ROLES 중 하나라도 가지고 있어야 접근이 가능하도록 설정
                    .anyRequest().hasAnyRole(PERMITTED_ROLES)
//                    .hasAnyRole(PERMITTED_ROLES)
            )

            // JWT 사용으로 인한 세션 미사용
            .sessionManagement(configurer -> configurer
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // JWT 검증 필터 추가
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//            .addFilterBefore(new ExceptionHandlerFilter(), JwtAuthenticationFilter.class)
        ;

        return http.build();
    }
}
