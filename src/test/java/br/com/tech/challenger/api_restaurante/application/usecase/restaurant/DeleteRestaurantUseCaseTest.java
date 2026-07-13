package br.com.tech.challenger.api_restaurante.application.usecase.restaurant;

import br.com.tech.challenger.api_restaurante.application.exception.RestaurantNotFoundException;
import br.com.tech.challenger.api_restaurante.domain.entity.Restaurant;
import br.com.tech.challenger.api_restaurante.domain.repository.RestaurantRepository;
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
class DeleteRestaurantUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    private DeleteRestaurantUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeleteRestaurantUseCase(restaurantRepository);
    }

    @Test
    void execute_notFound_thenThrow() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(99L))
                .isInstanceOf(RestaurantNotFoundException.class)
                .hasMessageContaining("Restaurant not found");

        verify(restaurantRepository, never()).deleteById(99L);
    }

    @Test
    void execute_success() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(Restaurant.builder().id(1L).build()));

        useCase.execute(1L);

        verify(restaurantRepository).deleteById(1L);
    }
}
