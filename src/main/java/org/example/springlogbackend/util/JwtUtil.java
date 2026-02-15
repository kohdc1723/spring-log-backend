package org.example.springlogbackend.util;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {
    private final SecretKey secretKey;
    private final long accessTokenExpiresIn;
    private final long refreshTokenExpiresIn;

    public JwtUtil(
            @Value("${spring.jwt.secret}") String secretKey,
            @Value("${spring.jwt.access-expiration}") long accessTokenExpiresIn,
            @Value("${spring.jwt.refresh-expiration}") long refreshTokenExpiresIn
    ) {
        this.secretKey = new SecretKeySpec(
                secretKey.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
        this.accessTokenExpiresIn = accessTokenExpiresIn;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }

    public String getSubject(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String getJti(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("jti", String.class);
    }

    public String getEmail(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("email", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public boolean isAccessTokenValid(String token) {
        String type = getType(token);
        Date expiration = getExpiration(token);

        return type.equals("access") && !expiration.before(new Date());
    }

    public boolean isRefreshTokenValid(String token) {
        String type = getType(token);
        Date expiration = getExpiration(token);

        return type.equals("refresh") && !expiration.before(new Date());
    }

    public String createAccessToken(String userId, String email, String role) {
        long now = System.currentTimeMillis();
        long expiration = now + accessTokenExpiresIn;

        return Jwts.builder()
                .subject(userId)
                .claim("jti", UUID.randomUUID().toString())
                .claim("email", email)
                .claim("role", role)
                .claim("type", "access")
                .issuedAt(new Date(now))
                .expiration(new Date(expiration))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(String userId, String email, String role) {
        long now = System.currentTimeMillis();
        long expiration = now + refreshTokenExpiresIn;

        return Jwts.builder()
                .subject(userId)
                .claim("jti", UUID.randomUUID().toString())
                .claim("email", email)
                .claim("role", role)
                .claim("type", "refresh")
                .issuedAt(new Date(now))
                .expiration(new Date(expiration))
                .signWith(secretKey)
                .compact();
    }

    private String getType(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("type", String.class);
    }

    private Date getExpiration(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }
}
