package br.com.tech.challenger.api_restaurante.infrastructure.persistence.mapper;

import br.com.tech.challenger.api_restaurante.domain.entity.MenuItem;
import br.com.tech.challenger.api_restaurante.domain.entity.Restaurant;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.MenuItemJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.RestaurantJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserTypeJpaEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class MenuItemMapperTest {

    private final MenuItemMapper mapper = new MenuItemMapper(new RestaurantMapper(new UserMapper(new UserTypeMapper(), new UserAddressMapper())));

    @Test
    void toDomain_mapsAllFieldsIncludingRestaurant() {
        LocalDateTime now = LocalDateTime.now();

        UserTypeJpaEntity userType = UserTypeJpaEntity.builder().id(1L).name("ADMIN").build();
        UserJpaEntity owner = UserJpaEntity.builder().id(1L).name("Owner").email("owner@email.com").login("owner").password("pwd").userType(userType).build();
        RestaurantJpaEntity restaurant = RestaurantJpaEntity.builder().id(1L).name("Restaurant").owner(owner).build();

        MenuItemJpaEntity entity = MenuItemJpaEntity.builder()
                .id(1L)
                .restaurant(restaurant)
                .name("Rice and Beans")
                .description("Rice and Beans")
                .price(BigDecimal.valueOf(15.20))
                .availableOnlyAtLocation(true)
                .imagePath("/images/rb.png")
                .createdAt(now)
                .updatedAt(now)
                .build();

        MenuItem domain = mapper.toDomain(entity);

        assertThat(domain.getId()).isEqualTo(1L);
        assertThat(domain.getName()).isEqualTo("Rice and Beans");
        assertThat(domain.getDescription()).isEqualTo("Rice and Beans");
        assertThat(domain.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(15.20));
        assertThat(domain.getAvailableOnlyAtLocation()).isTrue();
        assertThat(domain.getImagePath()).isEqualTo("/images/rb.png");
        assertThat(domain.getRestaurant().getId()).isEqualTo(1L);
        assertThat(domain.getRestaurant().getOwner().getId()).isEqualTo(1L);
        assertThat(domain.getCreatedAt()).isEqualTo(now);
        assertThat(domain.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void toDomain_whenNull_returnsNull() {
        assertThat(mapper.toDomain(null)).isNull();
    }

    @Test
    void toPersistence_mapsAllFieldsIncludingRestaurant() {
        LocalDateTime now = LocalDateTime.now();

        UserType userType = UserType.builder().id(1L).name("ADMIN").build();
        User owner = User.builder().id(1L).name("Owner").email("owner@email.com").login("owner").password("pwd").userType(userType).build();
        Restaurant restaurant = Restaurant.builder().id(1L).name("Restaurant").owner(owner).build();

        MenuItem domain = MenuItem.builder()
                .id(1L)
                .restaurant(restaurant)
                .name("Rice and Beans")
                .description("Rice and Beans")
                .price(BigDecimal.valueOf(15.20))
                .availableOnlyAtLocation(true)
                .imagePath("/images/rb.png")
                .createdAt(now)
                .updatedAt(now)
                .build();

        MenuItemJpaEntity entity = mapper.toPersistence(domain);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getName()).isEqualTo("Rice and Beans");
        assertThat(entity.getDescription()).isEqualTo("Rice and Beans");
        assertThat(entity.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(15.20));
        assertThat(entity.getAvailableOnlyAtLocation()).isTrue();
        assertThat(entity.getImagePath()).isEqualTo("/images/rb.png");
        assertThat(entity.getRestaurant().getId()).isEqualTo(1L);
        assertThat(entity.getRestaurant().getOwner().getId()).isEqualTo(1L);
        assertThat(entity.getCreatedAt()).isEqualTo(now);
        assertThat(entity.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void toPersistence_whenNull_returnsNull() {
        assertThat(mapper.toPersistence(null)).isNull();
    }
}
