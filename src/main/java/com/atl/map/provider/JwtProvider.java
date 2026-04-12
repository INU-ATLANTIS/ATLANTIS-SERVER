package com.atl.map.provider;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtProvider {

    private static final String ACCESS_TOKEN_CATEGORY = "access";
    private static final String REFRESH_TOKEN_CATEGORY = "refresh";

    @Value("${secret-key}")
    private String secretKey;

    @Value("${app.security.jwt.access-token-expiration-seconds:3600}")
    private long accessTokenExpirationSeconds;

    @Value("${app.security.jwt.refresh-token-expiration-seconds:604800}")
    private long refreshTokenExpirationSeconds;

    public String createAccessToken(String userEmail) {
        return createToken(userEmail, ACCESS_TOKEN_CATEGORY, accessTokenExpirationSeconds);
    }

    public String createRefreshToken(String userEmail) {
        return createToken(userEmail, REFRESH_TOKEN_CATEGORY, refreshTokenExpirationSeconds);
    }

    public String validateAccessToken(String jwt) {
        return validate(jwt, ACCESS_TOKEN_CATEGORY);
    }

    public String validateRefreshToken(String jwt) {
        return validate(jwt, REFRESH_TOKEN_CATEGORY);
    }

    public long getAccessTokenExpirationSeconds() {
        return accessTokenExpirationSeconds;
    }

    public long getRefreshTokenExpirationSeconds() {
        return refreshTokenExpirationSeconds;
    }

    private String createToken(String userEmail, String category, long expirationSeconds) {
        Date expiredDate = Date.from(Instant.now().plus(expirationSeconds, ChronoUnit.SECONDS));

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(userEmail)
                .claim("category", category)
                .setIssuedAt(new Date())
                .setExpiration(expiredDate)
                .compact();
    }

    private String validate(String jwt, String expectedCategory) {
        String subject = null;

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            subject = claims.getSubject();
            String category = claims.get("category", String.class);

            if (!expectedCategory.equals(category)) {
                log.warn("JWT 카테고리 불일치 - expected: {}, actual: {}", expectedCategory, category);
                return null;
            }
        }catch(Exception exception){
            log.error("JWT 검증 실패", exception);
            return null;
        }

        return subject;
    }
}
