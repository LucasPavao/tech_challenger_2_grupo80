package br.com.tech.challenger.api_restaurante.application.usecase.menuitem;

import br.com.tech.challenger.api_restaurante.application.exception.MenuItemNotFoundException;
import br.com.tech.challenger.api_restaurante.domain.entity.MenuItem;
import br.com.tech.challenger.api_restaurante.domain.repository.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteMenuItemUseCaseTest {

    @Mock
    private MenuItemRepository repository;

    private DeleteMenuItemUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeleteMenuItemUseCase(repository);
    }

    @Test
    void execute_notFound_thenThrow() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(99L))
                .isInstanceOf(MenuItemNotFoundException.class)
                .hasMessageContaining("Menu item not found with id: 99");

        verify(repository, never()).deleteById(99L);
    }

    @Test
    void execute_success() {
        when(repository.findById(1L)).thenReturn(Optional.of(MenuItem.builder().id(1L).build()));

        useCase.execute(1L);

        verify(repository).deleteById(1L);
    }
}
