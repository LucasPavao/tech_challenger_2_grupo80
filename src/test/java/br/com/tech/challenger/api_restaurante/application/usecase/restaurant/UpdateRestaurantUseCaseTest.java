package br.com.tech.challenger.api_restaurante.application.usecase.restaurant;

import br.com.tech.challenger.api_restaurante.application.dto.RestaurantRequestDTO;
import br.com.tech.challenger.api_restaurante.application.exception.RestaurantNotFoundException;
import br.com.tech.challenger.api_restaurante.application.exception.UserNotAnOwnerException;
import br.com.tech.challenger.api_restaurante.application.exception.UserNotFoundException;
import br.com.tech.challenger.api_restaurante.domain.entity.Restaurant;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.domain.enums.UserTypeEnum;
import br.com.tech.challenger.api_restaurante.domain.repository.RestaurantRepository;
import br.com.tech.challenger.api_restaurante.domain.repository.UserRepository;
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

    @Mock
    private UserRepository userRepository;

    private UpdateRestaurantUseCase useCase;

    private User originalOwner;
    private Restaurant exampleRestaurant;

    @BeforeEach
    void setUp() {
        useCase = new UpdateRestaurantUseCase(restaurantRepository, userRepository);

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

        RestaurantRequestDTO dto = new RestaurantRequestDTO("Restaurant", "Address", "Brazilian", "10:00-22:00", 1L);

        assertThatThrownBy(() -> useCase.execute(99L, dto))
                .isInstanceOf(RestaurantNotFoundException.class)
                .hasMessageContaining("Restaurant not found");

        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void execute_whenOwnerUnchanged_doesNotLookupUser() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(exampleRestaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(inv -> inv.getArgument(0));

        RestaurantRequestDTO dto = new RestaurantRequestDTO("Updated Name", "Address", "Brazilian", "10:00-22:00", 1L);

        Restaurant result = useCase.execute(1L, dto);

        assertThat(result.getName()).isEqualTo("Updated Name");
        assertThat(result.getOwner().getId()).isEqualTo(1L);
        verify(userRepository, never()).findById(any());
    }

    @Test
    void execute_whenOwnerChangedToValidOwner_updatesOwner() {
        UserType newOwnerType = UserType.builder().id(1L).name(UserTypeEnum.RESTAURANT_OWNER.name()).build();
        User newOwner = User.builder().id(2L).name("New Owner").login("newowner").userType(newOwnerType).build();

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(exampleRestaurant));
        when(userRepository.findById(2L)).thenReturn(Optional.of(newOwner));
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(inv -> inv.getArgument(0));

        RestaurantRequestDTO dto = new RestaurantRequestDTO("Restaurant", "Address", "Brazilian", "10:00-22:00", 2L);

        Restaurant result = useCase.execute(1L, dto);

        assertThat(result.getOwner().getId()).isEqualTo(2L);
    }

    @Test
    void execute_whenNewOwnerNotFound_thenThrow() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(exampleRestaurant));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        RestaurantRequestDTO dto = new RestaurantRequestDTO("Restaurant", "Address", "Brazilian", "10:00-22:00", 2L);

        assertThatThrownBy(() -> useCase.execute(1L, dto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void execute_whenNewOwnerIsNotAnOwner_thenThrow() {
        UserType customerType = UserType.builder().id(2L).name(UserTypeEnum.CUSTOMER.name()).build();
        User customer = User.builder().id(2L).name("Customer").login("customer").userType(customerType).build();

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(exampleRestaurant));
        when(userRepository.findById(2L)).thenReturn(Optional.of(customer));

        RestaurantRequestDTO dto = new RestaurantRequestDTO("Restaurant", "Address", "Brazilian", "10:00-22:00", 2L);

        assertThatThrownBy(() -> useCase.execute(1L, dto))
                .isInstanceOf(UserNotAnOwnerException.class)
                .hasMessageContaining("is not a restaurant owner");

        verify(restaurantRepository, never()).save(any());
    }
}
