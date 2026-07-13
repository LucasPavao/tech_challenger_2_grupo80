package br.com.tech.challenger.api_restaurante.application.usecase.restaurant;

import br.com.tech.challenger.api_restaurante.application.exception.RestaurantNotFoundException;
import br.com.tech.challenger.api_restaurante.domain.entity.Restaurant;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.domain.repository.RestaurantRepository;
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
class FindRestaurantByIdUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    private FindRestaurantByIdUseCase useCase;

    private Restaurant exampleRestaurant;

    @BeforeEach
    void setUp() {
        useCase = new FindRestaurantByIdUseCase(restaurantRepository);

        UserType userType = UserType.builder().id(1L).name("RESTAURANT_OWNER").build();
        User owner = User.builder().id(1L).name("Owner").userType(userType).build();
        exampleRestaurant = Restaurant.builder().id(1L).name("Restaurant").owner(owner).build();
    }

    @Test
    void execute_success() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(exampleRestaurant));

        Restaurant result = useCase.execute(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Restaurant");
    }

    @Test
    void execute_notFound_thenThrow() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(99L))
                .isInstanceOf(RestaurantNotFoundException.class)
                .hasMessageContaining("Restaurant not found");
    }
}
