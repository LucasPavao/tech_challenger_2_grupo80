package br.com.tech.challenger.api_restaurante.application.usecase.restaurant;

import br.com.tech.challenger.api_restaurante.application.dto.RestaurantRequestDTO;
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
class CreateRestaurantUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserRepository userRepository;

    private CreateRestaurantUseCase useCase;

    private User ownerUser;
    private RestaurantRequestDTO exampleRequest;

    @BeforeEach
    void setUp() {
        useCase = new CreateRestaurantUseCase(restaurantRepository, userRepository);

        UserType ownerType = UserType.builder().id(1L).name(UserTypeEnum.RESTAURANT_OWNER.name()).build();
        ownerUser = User.builder().id(1L).name("Owner").login("owner").userType(ownerType).build();

        exampleRequest = new RestaurantRequestDTO("Restaurant", "Address", "Brazilian", "10:00-22:00");
    }

    @Test
    void execute_whenOwnerNotFound_thenThrow() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(exampleRequest, 1L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void execute_whenUserIsNotAnOwner_thenThrow() {
        UserType customerType = UserType.builder().id(2L).name(UserTypeEnum.CUSTOMER.name()).build();
        User customer = User.builder().id(1L).name("Customer").login("customer").userType(customerType).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertThatThrownBy(() -> useCase.execute(exampleRequest, 1L))
                .isInstanceOf(UserNotAnOwnerException.class)
                .hasMessageContaining("is not a restaurant owner");

        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void execute_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(ownerUser));
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(inv -> inv.getArgument(0));

        Restaurant result = useCase.execute(exampleRequest, 1L);

        assertThat(result.getName()).isEqualTo("Restaurant");
        assertThat(result.getOwner().getId()).isEqualTo(1L);

        verify(restaurantRepository).save(any(Restaurant.class));
    }
}
