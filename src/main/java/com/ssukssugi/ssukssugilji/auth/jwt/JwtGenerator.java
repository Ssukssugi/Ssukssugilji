package com.ssukssugi.ssukssugilji.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class JwtGenerator {

    public String generateAccessToken(final Key ACCESS_SECRET, final long ACCESS_EXPIRATION,
        Long userId) {
        Long now = System.currentTimeMillis();

        return Jwts.builder()
            .setHeader(createHeader())
            .setClaims(createClaims(userId))
            .setSubject(String.valueOf(userId))
            .setExpiration(new Date(now + ACCESS_EXPIRATION))
            .signWith(ACCESS_SECRET, SignatureAlgorithm.HS256)
            .compact();
    }

    public String generateRefreshToken(final Key REFRESH_SECRET, final long REFRESH_EXPIRATION,
        Long userId) {
        Long now = System.currentTimeMillis();

        return Jwts.builder()
            .setHeader(createHeader())
            .setSubject(String.valueOf(userId))
            .setExpiration(new Date(now + REFRESH_EXPIRATION))
            .signWith(REFRESH_SECRET, SignatureAlgorithm.HS256)
            .compact();
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        return header;
    }

    private Map<String, Object> createClaims(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Identifier", userId);
        return claims;
    }
}