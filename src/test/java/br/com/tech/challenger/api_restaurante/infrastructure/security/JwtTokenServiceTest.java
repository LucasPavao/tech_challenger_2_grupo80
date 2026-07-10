package br.com.tech.challenger.api_restaurante.infrastructure.security;

import br.com.tech.challenger.api_restaurante.application.exception.InvalidTokenException;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenServiceTest {

    private static final String SECRET = "test-secret";

    private JwtTokenService jwtTokenService;
    private User exampleUser;

    @BeforeEach
    void setUp() {
        jwtTokenService = new JwtTokenService(SECRET, 60);

        UserType userType = UserType.builder().id(1L).name("ADMIN").build();
        exampleUser = User.builder().id(1L).login("john.smith").userType(userType).build();
    }

    @Test
    void generateTokenPair_thenValidateAccessToken_returnsLogin() {
        TokenPair tokenPair = jwtTokenService.generateTokenPair(exampleUser);

        String subject = jwtTokenService.validateAccessToken(tokenPair.accessToken());

        assertThat(subject).isEqualTo("john.smith");
        assertThat(tokenPair.expiresAt()).isAfter(Instant.now());
    }

    @Test
    void generateTokenPair_thenValidateRefreshToken_returnsUserId() {
        TokenPair tokenPair = jwtTokenService.generateTokenPair(exampleUser);

        String subject = jwtTokenService.validateRefreshToken(tokenPair.refreshToken());

        assertThat(subject).isEqualTo("1");
    }

    @Test
    void validateAccessToken_whenExpired_thenThrow() {
        String expiredToken = JWT.create()
                .withIssuer("Restaurante API")
                .withSubject("john.smith")
                .withExpiresAt(Instant.now().minus(1, ChronoUnit.HOURS))
                .sign(Algorithm.HMAC256(SECRET.getBytes()));

        assertThatThrownBy(() -> jwtTokenService.validateAccessToken(expiredToken))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void validateAccessToken_whenSignedWithDifferentSecret_thenThrow() {
        String foreignToken = JWT.create()
                .withIssuer("Restaurante API")
                .withSubject("john.smith")
                .withExpiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                .sign(Algorithm.HMAC256("another-secret".getBytes()));

        assertThatThrownBy(() -> jwtTokenService.validateAccessToken(foreignToken))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void validateAccessToken_whenTampered_thenThrow() {
        TokenPair tokenPair = jwtTokenService.generateTokenPair(exampleUser);
        String tampered = tokenPair.accessToken() + "x";

        assertThatThrownBy(() -> jwtTokenService.validateAccessToken(tampered))
                .isInstanceOf(InvalidTokenException.class);
    }
}
