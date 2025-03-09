package com.ssukssugi.ssukssugilji.auth.service;

import static com.ssukssugi.ssukssugilji.auth.jwt.JwtRule.ACCESS_PREFIX;
import static com.ssukssugi.ssukssugilji.auth.jwt.JwtRule.JWT_ISSUE_HEADER;
import static com.ssukssugi.ssukssugilji.auth.jwt.JwtRule.REFRESH_PREFIX;

import com.ssukssugi.ssukssugilji.auth.dao.TokenRepository;
import com.ssukssugi.ssukssugilji.auth.entity.Token;
import com.ssukssugi.ssukssugilji.auth.jwt.JwtGenerator;
import com.ssukssugi.ssukssugilji.auth.jwt.JwtRule;
import com.ssukssugi.ssukssugilji.auth.jwt.JwtTokenStatus;
import com.ssukssugi.ssukssugilji.auth.jwt.JwtUtil;
import com.ssukssugi.ssukssugilji.common.error.exception.InvalidRequestException;
import com.ssukssugi.ssukssugilji.user.entity.User;
import com.ssukssugi.ssukssugilji.user.service.UserService;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Key;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class JwtService {

    private final UserService userService;
    private final JwtGenerator jwtGenerator;
    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;

    private final Key ACCESS_SECRET_KEY;
    private final Key REFRESH_SECRET_KEY;
    private final long ACCESS_EXPIRATION;
    private final long REFRESH_EXPIRATION;

    public JwtService(UserService userService, JwtGenerator jwtGenerator,
        JwtUtil jwtUtil, TokenRepository tokenRepository,
        @Value("${jwt.access-secret}") String ACCESS_SECRET_KEY,
        @Value("${jwt.refresh-secret}") String REFRESH_SECRET_KEY,
        @Value("${jwt.access-expiration}") long ACCESS_EXPIRATION,
        @Value("${jwt.refresh-expiration}") long REFRESH_EXPIRATION) {
        this.userService = userService;
        this.jwtGenerator = jwtGenerator;
        this.jwtUtil = jwtUtil;
        this.tokenRepository = tokenRepository;
        this.ACCESS_SECRET_KEY = jwtUtil.getSigningKey(ACCESS_SECRET_KEY);
        this.REFRESH_SECRET_KEY = jwtUtil.getSigningKey(REFRESH_SECRET_KEY);
        this.ACCESS_EXPIRATION = ACCESS_EXPIRATION;
        this.REFRESH_EXPIRATION = REFRESH_EXPIRATION;
    }

    public String generateAccessToken(HttpServletResponse response, Long requestUserId) {
        String accessToken = jwtGenerator.generateAccessToken(ACCESS_SECRET_KEY, ACCESS_EXPIRATION,
            requestUserId);
        ResponseCookie cookie = setTokenToCookie(ACCESS_PREFIX.getValue(), accessToken,
            ACCESS_EXPIRATION / 1000);
        response.addHeader(JWT_ISSUE_HEADER.getValue(), cookie.toString());

        return accessToken;
    }

    @Transactional
    public void generateRefreshToken(HttpServletResponse response, Long requestUserId) {
        String refreshToken = jwtGenerator.generateRefreshToken(REFRESH_SECRET_KEY,
            REFRESH_EXPIRATION, requestUserId);
        ResponseCookie cookie = setTokenToCookie(REFRESH_PREFIX.getValue(), refreshToken,
            REFRESH_EXPIRATION / 1000);
        response.addHeader(JWT_ISSUE_HEADER.getValue(), cookie.toString());

        tokenRepository.save(Token
            .builder()
            .userId(requestUserId)
            .refreshToken(refreshToken)
            .build());
    }

    private ResponseCookie setTokenToCookie(String tokenPrefix, String token, long maxAgeSeconds) {
        return ResponseCookie.from(tokenPrefix, token)
            .path("/")
            .maxAge(maxAgeSeconds)
            .httpOnly(true)
            .sameSite("None")
            .secure(true)
            .build();
    }

    public boolean validateAccessToken(String token) {
        return jwtUtil.getTokenStatus(token, ACCESS_SECRET_KEY) == JwtTokenStatus.AUTHENTICATED;
    }

    public boolean validateRefreshToken(String token, Long userId) {
        boolean isRefreshValid =
            jwtUtil.getTokenStatus(token, REFRESH_SECRET_KEY) == JwtTokenStatus.AUTHENTICATED;

        Token storedToken = tokenRepository.findByUserId(userId);
        boolean isTokenMatched = storedToken.getRefreshToken().equals(token);

        return isRefreshValid && isTokenMatched;
    }

    public String resolveTokenFromCookie(HttpServletRequest request, JwtRule tokenPrefix) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new InvalidRequestException("JWT token not found in cookie");
        }
        return jwtUtil.resolveTokenFromCookie(cookies, tokenPrefix);
    }

    public Authentication getAuthentication(String token) {
        User user = userService.findById(getUserId(token, ACCESS_SECRET_KEY));
        return new UsernamePasswordAuthenticationToken(user, "USER");
    }

    private Long getUserId(String token, Key secretKey) {
        return Long.valueOf(Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject());
    }

    public Long getUserIdFromRefreshToken(String refreshToken) {
        try {
            return Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(REFRESH_SECRET_KEY)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody()
                .getSubject());
        } catch (NumberFormatException e) {
            throw new RuntimeException(
                "Format of userId from refreshToken is invalid; refreshToken: " + refreshToken);
        } catch (Exception e) {
            throw new InvalidRequestException("Invalid JWT token");
        }
    }

    public void logout(Long requestUserId, HttpServletResponse response) {
        tokenRepository.deleteByUserId(requestUserId);

        Cookie accessCookie = jwtUtil.resetToken(ACCESS_PREFIX);
        Cookie refreshCookie = jwtUtil.resetToken(REFRESH_PREFIX);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
    }
}