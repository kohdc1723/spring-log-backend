package org.example.springlogbackend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.example.springlogbackend.entity.ProviderType;
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

    public String getProvider(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("provider", String.class);
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

    public String createToken(
            String userId,
            ProviderType provider,
            String email,
            String role,
            JwtTokenType tokenType
    ) {
        long now = System.currentTimeMillis();
        long expiration = now + (tokenType == JwtTokenType.ACCESS ? accessTokenExpiresIn : refreshTokenExpiresIn);

        return Jwts.builder()
                .subject(userId)
                .claim("jti", UUID.randomUUID().toString())
                .claim("provider", provider)
                .claim("email", email)
                .claim("role", role)
                .claim("type", tokenType.getValue())
                .issuedAt(new Date(now))
                .expiration(new Date(expiration))
                .signWith(secretKey)
                .compact();
    }

    public boolean isValid(String token, JwtTokenType tokenType) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String type = claims.get("type", String.class);
            Date expiration = claims.getExpiration();

            if (type == null) return false;

            if (tokenType == JwtTokenType.ACCESS && !type.equals(JwtTokenType.ACCESS.getValue())) {
                return false;
            }

            if (tokenType == JwtTokenType.REFRESH && !type.equals(JwtTokenType.REFRESH.getValue())) {
                return false;
            }

            return !expiration.before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
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
