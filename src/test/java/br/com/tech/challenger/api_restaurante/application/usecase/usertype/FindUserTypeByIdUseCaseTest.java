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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindUserTypeByIdUseCaseTest {

    @Mock
    private UserTypeRepository repository;

    private FindUserTypeByIdUseCase useCase;

    private UserType exampleDomain;

    @BeforeEach
    void setUp() {
        useCase = new FindUserTypeByIdUseCase(repository);

        exampleDomain = UserType.builder()
                .id(1L)
                .name("Admin")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void execute_success() {
        when(repository.findById(1L)).thenReturn(Optional.of(exampleDomain));

        UserTypeDTO result = useCase.execute(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Admin");
    }

    @Test
    void execute_notFound_thenThrow() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User type not found");
    }
}
