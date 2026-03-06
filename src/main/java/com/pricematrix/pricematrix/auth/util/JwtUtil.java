package com.pricematrix.pricematrix.auth.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // 請換成你自己的 secret（32字元以上）
    private static final String SECRET = "pricematrix-secret-key-2026-secure!!";
    private static final long EXPIRATION_MS = 1000 * 60 * 60 * 8; // 8小時

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}