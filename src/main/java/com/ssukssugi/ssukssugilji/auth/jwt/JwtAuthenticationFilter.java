package com.ssukssugi.ssukssugilji.auth.jwt;

import static com.ssukssugi.ssukssugilji.common.configuration.SecurityConfig.PERMITTED_URI;

import com.ssukssugi.ssukssugilji.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Value("${api.auth.token}")
    private String apiAuthToken;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().equals("/favicon.ico")) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        if (isPermittedURI(request.getRequestURI())) {
            SecurityContextHolder.getContext().setAuthentication(null);
            filterChain.doFilter(request, response);
            return;
        }

        String authToken = request.getHeader("x-request-auth");
        if (authToken != null && Objects.equals(authToken, apiAuthToken)) {
            Long userId = Long.valueOf(request.getHeader("x-user-id"));
            SecurityContextHolder.getContext()
                .setAuthentication(jwtService.createUserAuthentication(userId));
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtService.resolveTokenFromCookie(request, JwtRule.ACCESS_PREFIX);
        if (jwtService.validateAccessToken(accessToken)) {
            setAuthenticationToContext(accessToken);
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = jwtService.resolveTokenFromCookie(request, JwtRule.REFRESH_PREFIX);
        Long userId = jwtService.getUserIdFromRefreshToken(refreshToken);

        if (jwtService.validateRefreshToken(refreshToken, userId)) {
            String reissuedAccessToken = jwtService.issueAndSetAccessToken(response, userId);
            jwtService.issueAndSetRefreshToken(response, userId);

            setAuthenticationToContext(reissuedAccessToken);
            filterChain.doFilter(request, response);
            return;
        }

        jwtService.logout(userId, response);
    }

    private boolean isPermittedURI(String requestURI) {
        return Arrays.stream(PERMITTED_URI)
            .anyMatch(permitted -> {
                String replace = permitted.replace("*", "");
                return requestURI.contains(replace) || replace.contains(requestURI);
            });
    }

    private void setAuthenticationToContext(String accessToken) {
        Authentication authentication = jwtService.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}