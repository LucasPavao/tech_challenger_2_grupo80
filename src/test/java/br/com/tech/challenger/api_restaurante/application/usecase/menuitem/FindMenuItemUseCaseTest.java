package br.com.tech.challenger.api_restaurante.application.usecase.menuitem;

import br.com.tech.challenger.api_restaurante.application.dto.MenuItemDTO;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindMenuItemUseCaseTest {

    @Mock
    private MenuItemRepository repository;

    private FindMenuItemUseCase useCase;

    private MenuItem exampleMenuItem;

    @BeforeEach
    void setUp() {
        useCase = new FindMenuItemUseCase(repository, new MenuItemDtoMapper(new RestaurantDtoMapper(new UserDtoMapper(new UserAddressDtoMapper()))));

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
    void execute_mapsCorrectly() {
        when(repository.findAll()).thenReturn(List.of(exampleMenuItem));

        List<MenuItemDTO> list = useCase.execute();

        assertThat(list).hasSize(1);
        assertThat(list.get(0).name()).isEqualTo("Rice and Beans");
    }

    @Test
    void executeByRestaurantId_mapsCorrectly() {
        when(repository.findByRestaurantId(1L)).thenReturn(List.of(exampleMenuItem));

        List<MenuItemDTO> list = useCase.executeByRestaurantId(1L);

        assertThat(list).hasSize(1);
        assertThat(list.get(0).name()).isEqualTo("Rice and Beans");
    }

}
