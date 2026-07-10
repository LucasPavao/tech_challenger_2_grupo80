package br.com.tech.challenger.api_restaurante.application.usecase.user;

import br.com.tech.challenger.api_restaurante.application.dto.UserAddressDTO;
import br.com.tech.challenger.api_restaurante.application.dto.UserDTO;
import br.com.tech.challenger.api_restaurante.application.exception.EmailAlreadyExistsException;
import br.com.tech.challenger.api_restaurante.application.exception.LoginAlreadyExistsException;
import br.com.tech.challenger.api_restaurante.application.exception.UserNotFoundException;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserTypeRepository userTypeRepository;

    private UserUseCase useCase;

    private UserType exampleUserType;
    private UserAddress exampleAddress;
    private User exampleUser;
    private UserAddressDTO exampleAddressDTO;
    private UserDTO exampleDto;

    @BeforeEach
    void setUp() {
        useCase = new UserUseCase(repository, userTypeRepository, new UserDtoMapper(new UserAddressDtoMapper()), new UserAddressDtoMapper());

        exampleUserType = UserType.builder()
                .id(1L)
                .name("ADMIN")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        exampleAddress = UserAddress.builder()
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
    void create_whenEmailAlreadyExists_thenThrow() {
        when(repository.existsByEmail("teste@email.com")).thenReturn(true);

        assertThatThrownBy(() -> useCase.create(exampleDto))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("Email already exists");

        verify(repository, never()).save(any());
    }

    @Test
    void create_whenLoginAlreadyExists_thenThrow() {
        when(repository.existsByEmail("teste@email.com")).thenReturn(false);
        when(repository.existsByLogin("login")).thenReturn(true);

        assertThatThrownBy(() -> useCase.create(exampleDto))
                .isInstanceOf(LoginAlreadyExistsException.class)
                .hasMessageContaining("Login already exists");

        verify(repository, never()).save(any());
    }

    @Test
    void create_whenUserTypeNotFound_thenThrow() {
        when(repository.existsByEmail("teste@email.com")).thenReturn(false);
        when(repository.existsByLogin("login")).thenReturn(false);
        when(userTypeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.create(exampleDto))
                .isInstanceOf(UserTypeNotFoundException.class)
                .hasMessageContaining("User type not found");

        verify(repository, never()).save(any());
    }

    @Test
    void create_success() {
        when(repository.existsByEmail("teste@email.com")).thenReturn(false);
        when(repository.existsByLogin("login")).thenReturn(false);
        when(userTypeRepository.findById(1L)).thenReturn(Optional.of(exampleUserType));
        when(repository.save(any(User.class))).thenReturn(exampleUser);

        UserDTO result = useCase.create(exampleDto);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.email()).isEqualTo("teste@email.com");
        assertThat(result.userTypeId()).isEqualTo(1L);

        verify(repository).save(any(User.class));
    }

    @Test
    void findAll_mapsCorrectly() {
        when(repository.findAll()).thenReturn(List.of(exampleUser));

        List<UserDTO> list = useCase.findAll();

        assertThat(list).hasSize(1);
        assertThat(list.get(0).id()).isEqualTo(1L);
        assertThat(list.get(0).email()).isEqualTo("teste@email.com");
    }

    @Test
    void findById_notFound_thenThrow() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.findById(1L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void findById_success() {
        when(repository.findById(1L)).thenReturn(Optional.of(exampleUser));

        UserDTO result = useCase.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.login()).isEqualTo("login");
    }

    @Test
    void findByName_mapsCorrectly() {
        when(repository.findByNameContaining("User")).thenReturn(List.of(exampleUser));

        List<UserDTO> list = useCase.findByName("User");

        assertThat(list).hasSize(1);
        assertThat(list.get(0).name()).isEqualTo("User");
    }

    @Test
    void findByEmail_notFound_thenThrow() {
        when(repository.findByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.findByEmail("naoexiste@email.com"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void findByLogin_notFound_thenThrow() {
        when(repository.findByLogin("naoexiste")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.findByLogin("naoexiste"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void update_success() {
        when(repository.findById(1L)).thenReturn(Optional.of(exampleUser));
        when(userTypeRepository.findById(1L)).thenReturn(Optional.of(exampleUserType));
        when(repository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserDTO updateDto = new UserDTO(null, "User Atualizado", "teste@email.com", "login", "novaSenha", 1L, exampleAddressDTO);

        UserDTO updated = useCase.update(1L, updateDto);

        assertThat(updated.name()).isEqualTo("User Atualizado");
        verify(repository).save(any(User.class));
    }

    @Test
    void update_whenEmailChangedAndAlreadyExists_thenThrow() {
        when(repository.findById(1L)).thenReturn(Optional.of(exampleUser));
        when(repository.existsByEmail("outro@email.com")).thenReturn(true);

        UserDTO updateDto = new UserDTO(null, "User", "outro@email.com", "login", "password", 1L, exampleAddressDTO);

        assertThatThrownBy(() -> useCase.update(1L, updateDto))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("Email already exists");

        verify(repository, never()).save(any());
    }

    @Test
    void update_whenLoginChangedAndAlreadyExists_thenThrow() {
        when(repository.findById(1L)).thenReturn(Optional.of(exampleUser));
        when(repository.existsByLogin("outroLogin")).thenReturn(true);

        UserDTO updateDto = new UserDTO(null, "User", "teste@email.com", "outroLogin", "password", 1L, exampleAddressDTO);

        assertThatThrownBy(() -> useCase.update(1L, updateDto))
                .isInstanceOf(LoginAlreadyExistsException.class)
                .hasMessageContaining("Login already exists");

        verify(repository, never()).save(any());
    }

    @Test
    void delete_notFound_thenThrow() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.delete(99L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(repository, never()).deleteById(any());
    }

    @Test
    void delete_success() {
        when(repository.findById(1L)).thenReturn(Optional.of(exampleUser));

        useCase.delete(1L);

        verify(repository).deleteById(1L);
    }
}
