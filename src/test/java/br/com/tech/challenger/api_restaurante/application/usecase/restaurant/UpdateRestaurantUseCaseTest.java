package br.com.tech.challenger.api_restaurante.application.usecase.restaurant;

import br.com.tech.challenger.api_restaurante.application.dto.RestaurantRequestDTO;
import br.com.tech.challenger.api_restaurante.application.exception.RestaurantNotFoundException;
import br.com.tech.challenger.api_restaurante.domain.entity.Restaurant;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.domain.enums.UserTypeEnum;
import br.com.tech.challenger.api_restaurante.domain.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateRestaurantUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    private UpdateRestaurantUseCase useCase;

    private User originalOwner;
    private Restaurant exampleRestaurant;

    @BeforeEach
    void setUp() {
        useCase = new UpdateRestaurantUseCase(restaurantRepository);

        UserType ownerType = UserType.builder().id(1L).name(UserTypeEnum.RESTAURANT_OWNER.name()).build();
        originalOwner = User.builder().id(1L).name("Owner").login("owner").userType(ownerType).build();

        exampleRestaurant = Restaurant.builder()
                .id(1L)
                .name("Restaurant")
                .address("Address")
                .cuisineType("Brazilian")
                .operatingHours("10:00-22:00")
                .owner(originalOwner)
                .build();
    }

    @Test
    void execute_whenRestaurantNotFound_thenThrow() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        RestaurantRequestDTO dto = new RestaurantRequestDTO("Restaurant", "Address", "Brazilian", "10:00-22:00");

        assertThatThrownBy(() -> useCase.execute(99L, dto))
                .isInstanceOf(RestaurantNotFoundException.class)
                .hasMessageContaining("Restaurant not found");

        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void execute_updatesFieldsButKeepsOriginalOwner() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(exampleRestaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(inv -> inv.getArgument(0));

        RestaurantRequestDTO dto = new RestaurantRequestDTO("Updated Name", "New Address", "Italian", "09:00-21:00");

        Restaurant result = useCase.execute(1L, dto);

        assertThat(result.getName()).isEqualTo("Updated Name");
        assertThat(result.getAddress()).isEqualTo("New Address");
        assertThat(result.getCuisineType()).isEqualTo("Italian");
        assertThat(result.getOperatingHours()).isEqualTo("09:00-21:00");
        assertThat(result.getOwner()).isEqualTo(originalOwner);
    }
}
