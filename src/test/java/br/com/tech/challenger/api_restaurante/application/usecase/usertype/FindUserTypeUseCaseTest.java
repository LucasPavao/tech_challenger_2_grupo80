package br.com.tech.challenger.api_restaurante.application.usecase.usertype;

import br.com.tech.challenger.api_restaurante.application.dto.UserTypeDTO;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.domain.repository.UserTypeRepository;
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
class FindUserTypeUseCaseTest {

    @Mock
    private UserTypeRepository repository;

    private FindUserTypeUseCase useCase;

    private UserType exampleDomain;

    @BeforeEach
    void setUp() {
        useCase = new FindUserTypeUseCase(repository);

        exampleDomain = UserType.builder()
                .id(1L)
                .name("Admin")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void execute_mapsCorrectly() {
        when(repository.findAll()).thenReturn(List.of(exampleDomain));

        List<UserTypeDTO> list = useCase.execute();

        assertThat(list).hasSize(1);
        assertThat(list.get(0).id()).isEqualTo(1L);
        assertThat(list.get(0).name()).isEqualTo("Admin");
    }
}
