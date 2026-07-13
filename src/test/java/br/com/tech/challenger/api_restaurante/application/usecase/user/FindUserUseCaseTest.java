package br.com.tech.challenger.api_restaurante.application.usecase.user;

import br.com.tech.challenger.api_restaurante.application.dto.UserDTO;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindUserUseCaseTest {

    @Mock
    private UserRepository repository;

    private FindUserUseCase useCase;

    private User exampleUser;

    @BeforeEach
    void setUp() {
        useCase = new FindUserUseCase(repository, new UserDtoMapper(new UserAddressDtoMapper()));

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
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void execute_mapsCorrectly() {
        when(repository.findAll()).thenReturn(List.of(exampleUser));

        List<UserDTO> list = useCase.execute();

        assertThat(list).hasSize(1);
        assertThat(list.get(0).id()).isEqualTo(1L);
        assertThat(list.get(0).email()).isEqualTo("teste@email.com");
    }

    @Test
    void executeByName_mapsCorrectly() {
        when(repository.findByNameContaining("User")).thenReturn(List.of(exampleUser));

        List<UserDTO> list = useCase.executeByName("User");

        assertThat(list).hasSize(1);
        assertThat(list.get(0).name()).isEqualTo("User");
    }
}
