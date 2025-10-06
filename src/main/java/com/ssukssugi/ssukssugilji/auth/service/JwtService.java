package com.ssukssugi.ssukssugilji.auth.service;

import static com.ssukssugi.ssukssugilji.auth.jwt.JwtRule.ACCESS_PREFIX;
import static com.ssukssugi.ssukssugilji.auth.jwt.JwtRule.REFRESH_PREFIX;

import com.ssukssugi.ssukssugilji.auth.dao.TokenRepository;
import com.ssukssugi.ssukssugilji.auth.entity.Token;
import com.ssukssugi.ssukssugilji.auth.jwt.JwtGenerator;
import com.ssukssugi.ssukssugilji.auth.jwt.JwtRule;
import com.ssukssugi.ssukssugilji.auth.jwt.JwtTokenStatus;
import com.ssukssugi.ssukssugilji.common.JwtUtil;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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

    public String issueAndSetAccessToken(HttpServletResponse response, Long requestUserId) {
        String accessToken = jwtGenerator.generateAccessToken(ACCESS_SECRET_KEY, ACCESS_EXPIRATION,
            requestUserId);
        Cookie cookie = new Cookie(ACCESS_PREFIX.getValue(), accessToken);
        cookie.setPath("/");
        cookie.setMaxAge((int) (ACCESS_EXPIRATION / 1000));
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);

        return accessToken;
    }

    @Transactional
    public void issueAndSetRefreshToken(HttpServletResponse response, Long requestUserId) {
        String refreshToken = jwtGenerator.generateRefreshToken(REFRESH_SECRET_KEY,
            REFRESH_EXPIRATION, requestUserId);
        Cookie cookie = new Cookie(REFRESH_PREFIX.getValue(), refreshToken);
        cookie.setPath("/");
        cookie.setMaxAge((int) (REFRESH_EXPIRATION / 1000));
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);

        upsertTokenDB(requestUserId, refreshToken);
    }

    private void upsertTokenDB(Long userId, String refreshToken) {
        Token entity = tokenRepository.findByUserId(userId)
            .orElseGet(Token::new);

        entity.setUserId(userId);
        entity.setRefreshToken(refreshToken);

        tokenRepository.save(entity);
    }

    public boolean validateAccessToken(String token) {
        return jwtUtil.getTokenStatus(token, ACCESS_SECRET_KEY) == JwtTokenStatus.AUTHENTICATED;
    }

    public boolean validateRefreshToken(String token, Long userId) {
        boolean isRefreshValid =
            jwtUtil.getTokenStatus(token, REFRESH_SECRET_KEY) == JwtTokenStatus.AUTHENTICATED;

        Token storedToken = tokenRepository.findByUserId(userId).orElseThrow(
            () -> new IllegalArgumentException("Token not found by userId: " + userId));
        boolean isTokenMatched = storedToken.getRefreshToken().equals(token);

        log.info("isRefreshValid: {}, isTokenMatched: {}, userId: {}",
            isRefreshValid, isTokenMatched, userId);

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
        Long userId = getUserId(token, ACCESS_SECRET_KEY);
        return createUserAuthentication(userId);
    }

    public Authentication createUserAuthentication(Long userId) {
        User user = userService.findById(userId);
        UserDetails userDetails = org.springframework.security.core.userdetails.User
            .withUsername("user")
            .password("")
            .roles("USER")
            .build();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user,
            userId, userDetails.getAuthorities());
        return auth;
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