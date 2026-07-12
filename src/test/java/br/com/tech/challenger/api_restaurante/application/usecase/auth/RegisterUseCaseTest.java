package br.com.tech.challenger.api_restaurante.application.usecase.auth;

import br.com.tech.challenger.api_restaurante.application.dto.AuthenticatedUserResponseDTO;
import br.com.tech.challenger.api_restaurante.application.dto.RegisterRequestDTO;
import br.com.tech.challenger.api_restaurante.application.dto.UserAddressDTO;
import br.com.tech.challenger.api_restaurante.application.exception.EmailAlreadyExistsException;
import br.com.tech.challenger.api_restaurante.application.exception.LoginAlreadyExistsException;
import br.com.tech.challenger.api_restaurante.application.exception.UserTypeNotFoundException;
import br.com.tech.challenger.api_restaurante.application.mapper.UserAddressDtoMapper;
import br.com.tech.challenger.api_restaurante.application.mapper.UserDtoMapper;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.entity.UserAddress;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.domain.repository.UserRepository;
import br.com.tech.challenger.api_restaurante.domain.repository.UserTypeRepository;
import br.com.tech.challenger.api_restaurante.infrastructure.security.JwtTokenService;
import br.com.tech.challenger.api_restaurante.infrastructure.security.TokenPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserTypeRepository userTypeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenService jwtTokenService;

    private RegisterUseCase useCase;

    private UserType customerType;
    private User savedUser;
    private UserAddressDTO exampleAddressDTO;
    private RegisterRequestDTO exampleDto;

    @BeforeEach
    void setUp() {
        useCase = new RegisterUseCase(
                userRepository,
                userTypeRepository,
                new UserDtoMapper(new UserAddressDtoMapper()),
                new UserAddressDtoMapper(),
                passwordEncoder,
                jwtTokenService);

        customerType = UserType.builder()
                .id(3L)
                .name("CUSTOMER")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        UserAddress exampleAddress = UserAddress.builder()
                .id(1L)
                .street("Rua Teste")
                .number("100")
                .city("São Paulo")
                .state("SP")
                .zipCode("00000-000")
                .country("Brasil")
                .build();

        savedUser = User.builder()
                .id(1L)
                .name("User")
                .email("teste@email.com")
                .login("login")
                .password("hashed-password")
                .userType(customerType)
                .userAddress(exampleAddress)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        exampleAddressDTO = new UserAddressDTO(null, "Rua Teste", "São Paulo", "100", null, "SP", "00000-000", "Brasil");
        exampleDto = new RegisterRequestDTO("User", "teste@email.com", "login", "password", exampleAddressDTO);
    }

    @Test
    void execute_whenEmailAlreadyExists_thenThrow() {
        when(userRepository.existsByEmail("teste@email.com")).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(exampleDto))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("Email already exists");

        verify(userRepository, never()).save(any());
    }

    @Test
    void execute_whenLoginAlreadyExists_thenThrow() {
        when(userRepository.existsByEmail("teste@email.com")).thenReturn(false);
        when(userRepository.existsByLogin("login")).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(exampleDto))
                .isInstanceOf(LoginAlreadyExistsException.class)
                .hasMessageContaining("Login already exists");

        verify(userRepository, never()).save(any());
    }

    @Test
    void execute_whenCustomerTypeNotFound_thenThrow() {
        when(userRepository.existsByEmail("teste@email.com")).thenReturn(false);
        when(userRepository.existsByLogin("login")).thenReturn(false);
        when(userTypeRepository.findByName("CUSTOMER")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(exampleDto))
                .isInstanceOf(UserTypeNotFoundException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void execute_success_alwaysAssignsCustomerTypeAndHashesPassword() {
        when(userRepository.existsByEmail("teste@email.com")).thenReturn(false);
        when(userRepository.existsByLogin("login")).thenReturn(false);
        when(userTypeRepository.findByName("CUSTOMER")).thenReturn(Optional.of(customerType));
        when(passwordEncoder.encode("password")).thenReturn("hashed-password");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtTokenService.generateTokenPair(savedUser))
                .thenReturn(new TokenPair("access-token", "refresh-token", Instant.now().plusSeconds(3600)));

        AuthenticatedUserResponseDTO result = useCase.execute(exampleDto);

        assertThat(result.user().id()).isEqualTo(1L);
        assertThat(result.user().email()).isEqualTo("teste@email.com");
        assertThat(result.user().userTypeId()).isEqualTo(3L);
        assertThat(result.token().accessToken()).isEqualTo("access-token");
        assertThat(result.token().refreshToken()).isEqualTo("refresh-token");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getPassword()).isEqualTo("hashed-password");
        assertThat(userCaptor.getValue().getUserType()).isEqualTo(customerType);
    }
}
