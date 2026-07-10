package br.com.tech.challenger.api_restaurante.application.usecase.restaurant;

import br.com.tech.challenger.api_restaurante.domain.entity.Restaurant;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.domain.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindRestaurantUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    private FindRestaurantUseCase useCase;

    private Restaurant exampleRestaurant;

    @BeforeEach
    void setUp() {
        useCase = new FindRestaurantUseCase(restaurantRepository);

        UserType userType = UserType.builder().id(1L).name("RESTAURANT_OWNER").build();
        User owner = User.builder().id(1L).name("Owner").userType(userType).build();
        exampleRestaurant = Restaurant.builder().id(1L).name("Restaurant").owner(owner).build();
    }

    @Test
    void execute_returnsAllRestaurants() {
        when(restaurantRepository.findAll()).thenReturn(List.of(exampleRestaurant));

        List<Restaurant> result = useCase.execute();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Restaurant");
    }
}
