package br.com.tech.challenger.api_restaurante.application.usecase.user;

import br.com.tech.challenger.api_restaurante.application.dto.UserAddressDTO;
import br.com.tech.challenger.api_restaurante.application.dto.UserDTO;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserTypeRepository userTypeRepository;

    private CreateUserUseCase useCase;

    private UserType exampleUserType;
    private User exampleUser;
    private UserAddressDTO exampleAddressDTO;
    private UserDTO exampleDto;

    @BeforeEach
    void setUp() {
        useCase = new CreateUserUseCase(repository, userTypeRepository, new UserDtoMapper(new UserAddressDtoMapper()), new UserAddressDtoMapper());

        exampleUserType = UserType.builder()
                .id(1L)
                .name("ADMIN")
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

        exampleUser = User.builder()
                .id(1L)
                .name("User")
                .email("teste@email.com")
                .login("login")
                .password("password")
                .userType(exampleUserType)
                .userAddress(exampleAddress)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        exampleAddressDTO = new UserAddressDTO(null, "Rua Teste", "São Paulo", "100", null, "SP", "00000-000", "Brasil");
        exampleDto = new UserDTO(null, "User", "teste@email.com", "login", "password", 1L, exampleAddressDTO);
    }

    @Test
    void execute_whenEmailAlreadyExists_thenThrow() {
        when(repository.existsByEmail("teste@email.com")).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(exampleDto))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("Email already exists");

        verify(repository, never()).save(any());
    }

    @Test
    void execute_whenLoginAlreadyExists_thenThrow() {
        when(repository.existsByEmail("teste@email.com")).thenReturn(false);
        when(repository.existsByLogin("login")).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(exampleDto))
                .isInstanceOf(LoginAlreadyExistsException.class)
                .hasMessageContaining("Login already exists");

        verify(repository, never()).save(any());
    }

    @Test
    void execute_whenUserTypeNotFound_thenThrow() {
        when(repository.existsByEmail("teste@email.com")).thenReturn(false);
        when(repository.existsByLogin("login")).thenReturn(false);
        when(userTypeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(exampleDto))
                .isInstanceOf(UserTypeNotFoundException.class)
                .hasMessageContaining("User type not found");

        verify(repository, never()).save(any());
    }

    @Test
    void execute_success() {
        when(repository.existsByEmail("teste@email.com")).thenReturn(false);
        when(repository.existsByLogin("login")).thenReturn(false);
        when(userTypeRepository.findById(1L)).thenReturn(Optional.of(exampleUserType));
        when(repository.save(any(User.class))).thenReturn(exampleUser);

        UserDTO result = useCase.execute(exampleDto);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.email()).isEqualTo("teste@email.com");
        assertThat(result.userTypeId()).isEqualTo(1L);

        verify(repository).save(any(User.class));
    }
}
