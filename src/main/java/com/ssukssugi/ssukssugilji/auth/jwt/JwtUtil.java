package com.ssukssugi.ssukssugilji.auth.jwt;

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
        } catch (ExpiredJwtException | IllegalArgumentException e) {
//            log.error(INVALID_EXPIRED_JWT.getMessage());
            return JwtTokenStatus.EXPIRED;
        } catch (JwtException e) {
            throw new InvalidRequestException("Invalid JWT token");
        }
    }

    public String resolveTokenFromCookie(Cookie[] cookies, JwtRule tokenPrefix) {
        return Arrays.stream(cookies)
            .filter(cookie -> cookie.getName().equals(tokenPrefix.getValue()))
            .findFirst()
            .map(Cookie::getValue)
            .orElse("");
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
