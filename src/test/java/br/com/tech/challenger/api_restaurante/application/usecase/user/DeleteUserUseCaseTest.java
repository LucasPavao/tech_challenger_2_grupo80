package br.com.tech.challenger.api_restaurante.application.usecase.user;

import br.com.tech.challenger.api_restaurante.application.exception.UserNotFoundException;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseTest {

    @Mock
    private UserRepository repository;

    private DeleteUserUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeleteUserUseCase(repository);
    }

    @Test
    void execute_notFound_thenThrow() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(99L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(repository, never()).deleteById(any());
    }

    @Test
    void execute_success() {
        when(repository.findById(1L)).thenReturn(Optional.of(User.builder().id(1L).build()));

        useCase.execute(1L);

        verify(repository).deleteById(1L);
    }
}
