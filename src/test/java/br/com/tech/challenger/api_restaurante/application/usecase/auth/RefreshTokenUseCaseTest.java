package br.com.tech.challenger.api_restaurante.application.usecase.auth;

import br.com.tech.challenger.api_restaurante.application.dto.RefreshTokenRequestDTO;
import br.com.tech.challenger.api_restaurante.application.dto.TokenResponseDTO;
import br.com.tech.challenger.api_restaurante.application.exception.InvalidTokenException;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.domain.repository.UserRepository;
import br.com.tech.challenger.api_restaurante.infrastructure.security.JwtTokenService;
import br.com.tech.challenger.api_restaurante.infrastructure.security.TokenPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenUseCaseTest {

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private UserRepository userRepository;

    private RefreshTokenUseCase useCase;

    private User exampleUser;

    @BeforeEach
    void setUp() {
        useCase = new RefreshTokenUseCase(jwtTokenService, userRepository);

        UserType userType = UserType.builder().id(1L).name("ADMIN").build();
        exampleUser = User.builder().id(1L).login("john.smith").userType(userType).build();
    }

    @Test
    void execute_success() {
        when(jwtTokenService.validateRefreshToken("refresh-token")).thenReturn("1");
        when(userRepository.findById(1L)).thenReturn(Optional.of(exampleUser));

        TokenPair tokenPair = new TokenPair("new-access-token", "new-refresh-token", Instant.now().plusSeconds(3600));
        when(jwtTokenService.generateTokenPair(exampleUser)).thenReturn(tokenPair);

        TokenResponseDTO result = useCase.execute(new RefreshTokenRequestDTO("refresh-token"));

        assertThat(result.accessToken()).isEqualTo("new-access-token");
        assertThat(result.refreshToken()).isEqualTo("new-refresh-token");
    }

    @Test
    void execute_whenUserNoLongerExists_thenThrow() {
        when(jwtTokenService.validateRefreshToken("refresh-token")).thenReturn("99");
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(new RefreshTokenRequestDTO("refresh-token")))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void execute_whenTokenInvalid_thenPropagatesException() {
        when(jwtTokenService.validateRefreshToken("bad-token")).thenThrow(new InvalidTokenException("Invalid or expired token"));

        assertThatThrownBy(() -> useCase.execute(new RefreshTokenRequestDTO("bad-token")))
                .isInstanceOf(InvalidTokenException.class);
    }
}
