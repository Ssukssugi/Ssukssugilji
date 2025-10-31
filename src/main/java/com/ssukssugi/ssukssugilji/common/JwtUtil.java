package com.ssukssugi.ssukssugilji.common;

import com.ssukssugi.ssukssugilji.auth.jwt.JwtRule;
import com.ssukssugi.ssukssugilji.auth.jwt.JwtTokenStatus;
import com.ssukssugi.ssukssugilji.common.error.exception.InvalidRequestException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JwtUtil {

    public JwtTokenStatus getTokenStatus(String token, Key secretKey) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
            return JwtTokenStatus.AUTHENTICATED;
        } catch (ExpiredJwtException e) {
            return JwtTokenStatus.EXPIRED;
        } catch (JwtException | IllegalArgumentException e) {
            return JwtTokenStatus.INVALID;
        }
    }

    public String resolveTokenFromCookie(Cookie[] cookies, JwtRule tokenPrefix) {
        return Arrays.stream(cookies)
            .filter(cookie -> cookie.getName().equals(tokenPrefix.getValue()))
            .findFirst()
            .map(Cookie::getValue)
            .orElseThrow(
                () -> new InvalidRequestException(
                    tokenPrefix.getValue() + " Token not found in cookies"));
    }

    public Key getSigningKey(String secretKey) {
        String encodedKey = encodeToBase64(secretKey);
        return Keys.hmacShaKeyFor(encodedKey.getBytes(StandardCharsets.UTF_8));
    }

    private String encodeToBase64(String secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public Cookie resetToken(JwtRule tokenPrefix) {
        Cookie cookie = new Cookie(tokenPrefix.getValue(), null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }
}
