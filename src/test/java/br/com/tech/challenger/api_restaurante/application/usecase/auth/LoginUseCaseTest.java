package br.com.tech.challenger.api_restaurante.application.usecase.auth;

import br.com.tech.challenger.api_restaurante.application.dto.AuthenticatedUserResponseDTO;
import br.com.tech.challenger.api_restaurante.application.dto.LoginRequestDTO;
import br.com.tech.challenger.api_restaurante.application.mapper.UserAddressDtoMapper;
import br.com.tech.challenger.api_restaurante.application.mapper.UserDtoMapper;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.infrastructure.security.JwtTokenService;
import br.com.tech.challenger.api_restaurante.infrastructure.security.TokenPair;
import br.com.tech.challenger.api_restaurante.infrastructure.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private Authentication authentication;

    private LoginUseCase useCase;

    private User exampleUser;

    @BeforeEach
    void setUp() {
        useCase = new LoginUseCase(authenticationManager, jwtTokenService, new UserDtoMapper(new UserAddressDtoMapper()));

        UserType userType = UserType.builder().id(1L).name("ADMIN").build();
        exampleUser = User.builder().id(1L).name("John Smith").email("john@email.com").login("john.smith").userType(userType).build();
    }

    @Test
    void execute_success() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(new UserPrincipal(exampleUser));

        TokenPair tokenPair = new TokenPair("access-token", "refresh-token", Instant.now().plusSeconds(3600));
        when(jwtTokenService.generateTokenPair(exampleUser)).thenReturn(tokenPair);

        AuthenticatedUserResponseDTO result = useCase.execute(new LoginRequestDTO("john.smith", "password"));

        assertThat(result.user().login()).isEqualTo("john.smith");
        assertThat(result.token().accessToken()).isEqualTo("access-token");
        assertThat(result.token().refreshToken()).isEqualTo("refresh-token");
    }
}
