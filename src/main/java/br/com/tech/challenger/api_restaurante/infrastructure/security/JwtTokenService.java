package br.com.tech.challenger.api_restaurante.infrastructure.security;

import br.com.tech.challenger.api_restaurante.application.exception.InvalidTokenException;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class JwtTokenService {

    private static final String ISSUER = "Restaurante API";

    private final String secret;
    private final int expirationMinutes;

    public JwtTokenService(
            @Value("${security.secret}") String secret,
            @Value("${security.expiration.minutes}") int expirationMinutes) {
        this.secret = secret;
        this.expirationMinutes = expirationMinutes;
    }

    public TokenPair generateTokenPair(User user) {
        Instant accessExpiresAt = expirationInstant(expirationMinutes);
        String accessToken = buildToken(user.getLogin(), accessExpiresAt);
        String refreshToken = buildToken(user.getId().toString(), expirationInstant(expirationMinutes * 3));
        return new TokenPair(accessToken, refreshToken, accessExpiresAt);
    }

    public String validateAccessToken(String token) {
        return validate(token);
    }

    public String validateRefreshToken(String token) {
        return validate(token);
    }

    private String buildToken(String subject, Instant expiresAt) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(subject)
                .withExpiresAt(expiresAt)
                .sign(algorithm);
    }

    private String validate(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
            DecodedJWT decoded = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token);
            return decoded.getSubject();
        } catch (JWTVerificationException exception) {
            throw new InvalidTokenException("Invalid or expired token");
        }
    }

    private Instant expirationInstant(int minutes) {
        return LocalDateTime.now().plusMinutes(minutes).toInstant(ZoneOffset.of("-03:00"));
    }
}
