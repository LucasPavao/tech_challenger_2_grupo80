package br.com.tech.challenger.api_restaurante.application.usecase.menuitem;

import br.com.tech.challenger.api_restaurante.application.dto.MenuItemDTO;
import br.com.tech.challenger.api_restaurante.application.dto.MenuItemRequestDTO;
import br.com.tech.challenger.api_restaurante.application.exception.MenuItemNotFoundException;
import br.com.tech.challenger.api_restaurante.application.exception.RestaurantNotFoundException;
import br.com.tech.challenger.api_restaurante.application.mapper.MenuItemDtoMapper;
import br.com.tech.challenger.api_restaurante.application.mapper.RestaurantDtoMapper;
import br.com.tech.challenger.api_restaurante.application.mapper.UserAddressDtoMapper;
import br.com.tech.challenger.api_restaurante.application.mapper.UserDtoMapper;
import br.com.tech.challenger.api_restaurante.domain.entity.MenuItem;
import br.com.tech.challenger.api_restaurante.domain.entity.Restaurant;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.domain.repository.MenuItemRepository;
import br.com.tech.challenger.api_restaurante.domain.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateMenuItemUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    private UpdateMenuItemUseCase useCase;

    private Restaurant exampleRestaurant;
    private MenuItem exampleMenuItem;
    private MenuItemRequestDTO exampleRequest;

    @BeforeEach
    void setUp() {
        useCase = new UpdateMenuItemUseCase(menuItemRepository, restaurantRepository,
                new MenuItemDtoMapper(new RestaurantDtoMapper(new UserDtoMapper(new UserAddressDtoMapper()))));

        UserType userType = UserType.builder().id(1L).name("ADMIN").build();
        User user = User.builder().id(1L).email("email").name("User").userType(userType).build();
        exampleRestaurant = Restaurant.builder().id(1L).name("Test Restaurant").owner(user).build();

        exampleMenuItem = MenuItem.builder()
                .id(1L)
                .restaurant(exampleRestaurant)
                .name("Rice and Beans")
                .description("Rice and Beans")
                .price(BigDecimal.valueOf(15.20))
                .availableOnlyAtLocation(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        exampleRequest = new MenuItemRequestDTO("Rice and Beans Updated", "New description",
                BigDecimal.valueOf(18.50), false, null, 1L);
    }

    @Test
    void execute_whenMenuItemNotFound_thenThrow() {
        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(1L, exampleRequest))
                .isInstanceOf(MenuItemNotFoundException.class)
                .hasMessageContaining("Menu item not found with id: 1");

        verify(menuItemRepository, never()).save(any());
    }

    @Test
    void execute_whenRestaurantNotFound_thenThrow() {
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(exampleMenuItem));
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(1L, exampleRequest))
                .isInstanceOf(RestaurantNotFoundException.class)
                .hasMessageContaining("Restaurant not found with id: 1");

        verify(menuItemRepository, never()).save(any());
    }

    @Test
    void execute_success() {
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(exampleMenuItem));
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(exampleRestaurant));
        when(menuItemRepository.save(any(MenuItem.class))).thenAnswer(inv -> inv.getArgument(0));

        MenuItemDTO result = useCase.execute(1L, exampleRequest);

        assertThat(result.name()).isEqualTo("Rice and Beans Updated");
        assertThat(result.description()).isEqualTo("New description");
        assertThat(result.price()).isEqualByComparingTo(BigDecimal.valueOf(18.50));

        verify(menuItemRepository).save(any(MenuItem.class));
    }
}
