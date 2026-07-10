package br.com.tech.challenger.api_restaurante.application.usecase.usertype;

import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.domain.repository.UserTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteUserTypeUseCaseTest {

    @Mock
    private UserTypeRepository repository;

    private DeleteUserTypeUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeleteUserTypeUseCase(repository);
    }

    @Test
    void execute_notFound_thenThrow() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User type not found");
    }

    @Test
    void execute_success() {
        when(repository.findById(1L)).thenReturn(Optional.of(UserType.builder().id(1L).build()));

        useCase.execute(1L);

        verify(repository).deleteById(1L);
    }
}
