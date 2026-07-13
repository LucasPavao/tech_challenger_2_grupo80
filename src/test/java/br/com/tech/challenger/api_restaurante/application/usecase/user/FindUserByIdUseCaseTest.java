package br.com.tech.challenger.api_restaurante.application.usecase.user;

import br.com.tech.challenger.api_restaurante.application.dto.UserDTO;
import br.com.tech.challenger.api_restaurante.application.exception.UserNotFoundException;
import br.com.tech.challenger.api_restaurante.application.mapper.UserAddressDtoMapper;
import br.com.tech.challenger.api_restaurante.application.mapper.UserDtoMapper;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.entity.UserAddress;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindUserByIdUseCaseTest {

    @Mock
    private UserRepository repository;

    private FindUserByIdUseCase useCase;

    private User exampleUser;

    @BeforeEach
    void setUp() {
        useCase = new FindUserByIdUseCase(repository, new UserDtoMapper(new UserAddressDtoMapper()));

        UserType userType = UserType.builder().id(1L).name("ADMIN").build();
        UserAddress address = UserAddress.builder()
                .id(1L).street("Rua Teste").number("100").city("São Paulo").state("SP")
                .zipCode("00000-000").country("Brasil").build();

        exampleUser = User.builder()
                .id(1L)
                .name("User")
                .email("teste@email.com")
                .login("login")
                .password("password")
                .userType(userType)
                .userAddress(address)
                .build();
    }

    @Test
    void execute_success() {
        when(repository.findById(1L)).thenReturn(Optional.of(exampleUser));

        UserDTO result = useCase.execute(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.login()).isEqualTo("login");
    }

    @Test
    void execute_notFound_thenThrow() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(1L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");
    }
}
