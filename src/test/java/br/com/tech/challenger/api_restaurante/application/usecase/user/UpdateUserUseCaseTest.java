package br.com.tech.challenger.api_restaurante.application.usecase.user;

import br.com.tech.challenger.api_restaurante.application.dto.UserAddressDTO;
import br.com.tech.challenger.api_restaurante.application.dto.UserDTO;
import br.com.tech.challenger.api_restaurante.application.exception.EmailAlreadyExistsException;
import br.com.tech.challenger.api_restaurante.application.exception.LoginAlreadyExistsException;
import br.com.tech.challenger.api_restaurante.application.exception.UserNotFoundException;
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
class UpdateUserUseCaseTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserTypeRepository userTypeRepository;

    private UpdateUserUseCase useCase;

    private UserType exampleUserType;
    private User exampleUser;
    private UserAddressDTO exampleAddressDTO;

    @BeforeEach
    void setUp() {
        useCase = new UpdateUserUseCase(repository, userTypeRepository, new UserDtoMapper(new UserAddressDtoMapper()), new UserAddressDtoMapper());

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
    }

    @Test
    void execute_notFound_thenThrow() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        UserDTO dto = new UserDTO(null, "User", "teste@email.com", "login", "password", 1L, exampleAddressDTO);

        assertThatThrownBy(() -> useCase.execute(1L, dto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(repository, never()).save(any());
    }

    @Test
    void execute_success() {
        when(repository.findById(1L)).thenReturn(Optional.of(exampleUser));
        when(userTypeRepository.findById(1L)).thenReturn(Optional.of(exampleUserType));
        when(repository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserDTO updateDto = new UserDTO(null, "User Atualizado", "teste@email.com", "login", "novaSenha", 1L, exampleAddressDTO);

        UserDTO updated = useCase.execute(1L, updateDto);

        assertThat(updated.name()).isEqualTo("User Atualizado");
        verify(repository).save(any(User.class));
    }

    @Test
    void execute_whenEmailChangedAndAlreadyExists_thenThrow() {
        when(repository.findById(1L)).thenReturn(Optional.of(exampleUser));
        when(repository.existsByEmail("outro@email.com")).thenReturn(true);

        UserDTO updateDto = new UserDTO(null, "User", "outro@email.com", "login", "password", 1L, exampleAddressDTO);

        assertThatThrownBy(() -> useCase.execute(1L, updateDto))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("Email already exists");

        verify(repository, never()).save(any());
    }

    @Test
    void execute_whenLoginChangedAndAlreadyExists_thenThrow() {
        when(repository.findById(1L)).thenReturn(Optional.of(exampleUser));
        when(repository.existsByLogin("outroLogin")).thenReturn(true);

        UserDTO updateDto = new UserDTO(null, "User", "teste@email.com", "outroLogin", "password", 1L, exampleAddressDTO);

        assertThatThrownBy(() -> useCase.execute(1L, updateDto))
                .isInstanceOf(LoginAlreadyExistsException.class)
                .hasMessageContaining("Login already exists");

        verify(repository, never()).save(any());
    }
}
