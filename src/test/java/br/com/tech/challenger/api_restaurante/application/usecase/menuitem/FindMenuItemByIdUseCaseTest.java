package br.com.tech.challenger.api_restaurante.application.usecase.menuitem;

import br.com.tech.challenger.api_restaurante.application.dto.MenuItemDTO;
import br.com.tech.challenger.api_restaurante.application.exception.MenuItemNotFoundException;
import br.com.tech.challenger.api_restaurante.application.mapper.MenuItemDtoMapper;
import br.com.tech.challenger.api_restaurante.application.mapper.RestaurantDtoMapper;
import br.com.tech.challenger.api_restaurante.application.mapper.UserAddressDtoMapper;
import br.com.tech.challenger.api_restaurante.application.mapper.UserDtoMapper;
import br.com.tech.challenger.api_restaurante.domain.entity.MenuItem;
import br.com.tech.challenger.api_restaurante.domain.entity.Restaurant;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.domain.repository.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FindMenuItemByIdUseCaseTest {

    @Mock
    private MenuItemRepository repository;

    private FindMenuItemByIdUseCase useCase;

    private MenuItem exampleMenuItem;

    @BeforeEach
    void setUp() {
        useCase = new FindMenuItemByIdUseCase(repository, new MenuItemDtoMapper(new RestaurantDtoMapper(new UserDtoMapper(new UserAddressDtoMapper()))));

        UserType userType = UserType.builder().id(1L).name("ADMIN").build();
        User user = User.builder().id(1L).email("email").name("User").userType(userType).build();
        Restaurant restaurant = Restaurant.builder().id(1L).name("Test Restaurant").owner(user).build();

        exampleMenuItem = MenuItem.builder()
                .id(1L)
                .restaurant(restaurant)
                .name("Rice and Beans")
                .description("Rice and Beans")
                .price(BigDecimal.valueOf(15.20))
                .availableOnlyAtLocation(true)
                .imagePath(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void execute_success() {
        when(repository.findById(1L)).thenReturn(Optional.of(exampleMenuItem));

        MenuItemDTO result =  useCase.execute(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Rice and Beans");
    }

    @Test
    void execute_notFound_thenThrow() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(1L))
                .isInstanceOf(MenuItemNotFoundException.class)
                .hasMessage("Menu item not found with id: " + 1L);
    }

}
