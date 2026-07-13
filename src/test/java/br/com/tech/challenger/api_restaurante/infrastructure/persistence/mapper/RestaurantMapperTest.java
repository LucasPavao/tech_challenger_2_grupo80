package br.com.tech.challenger.api_restaurante.infrastructure.persistence.mapper;

import br.com.tech.challenger.api_restaurante.domain.entity.Restaurant;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.RestaurantJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserTypeJpaEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class RestaurantMapperTest {

    private final RestaurantMapper mapper = new RestaurantMapper(new UserMapper(new UserTypeMapper(), new UserAddressMapper()));

    @Test
    void toDomain_mapsAllFieldsIncludingOwner() {
        LocalDateTime now = LocalDateTime.now();

        UserTypeJpaEntity userType = UserTypeJpaEntity.builder().id(1L).name("RESTAURANT_OWNER").build();
        UserJpaEntity owner = UserJpaEntity.builder().id(1L).name("Owner").email("owner@email.com").login("owner").password("pwd").userType(userType).build();

        RestaurantJpaEntity entity = RestaurantJpaEntity.builder()
                .id(1L)
                .name("Restaurant")
                .address("Address")
                .cuisineType("Brazilian")
                .operatingHours("10:00-22:00")
                .owner(owner)
                .createdAt(now)
                .updatedAt(now)
                .build();

        Restaurant domain = mapper.toDomain(entity);

        assertThat(domain.getId()).isEqualTo(1L);
        assertThat(domain.getName()).isEqualTo("Restaurant");
        assertThat(domain.getAddress()).isEqualTo("Address");
        assertThat(domain.getCuisineType()).isEqualTo("Brazilian");
        assertThat(domain.getOperatingHours()).isEqualTo("10:00-22:00");
        assertThat(domain.getOwner().getId()).isEqualTo(1L);
        assertThat(domain.getOwner().getLogin()).isEqualTo("owner");
        assertThat(domain.getCreatedAt()).isEqualTo(now);
        assertThat(domain.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void toDomain_whenNull_returnsNull() {
        assertThat(mapper.toDomain(null)).isNull();
    }

    @Test
    void toPersistence_mapsAllFieldsIncludingOwner() {
        LocalDateTime now = LocalDateTime.now();

        UserType userType = UserType.builder().id(1L).name("RESTAURANT_OWNER").build();
        User owner = User.builder().id(1L).name("Owner").email("owner@email.com").login("owner").password("pwd").userType(userType).build();

        Restaurant domain = Restaurant.builder()
                .id(1L)
                .name("Restaurant")
                .address("Address")
                .cuisineType("Brazilian")
                .operatingHours("10:00-22:00")
                .owner(owner)
                .createdAt(now)
                .updatedAt(now)
                .build();

        RestaurantJpaEntity entity = mapper.toPersistence(domain);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getName()).isEqualTo("Restaurant");
        assertThat(entity.getAddress()).isEqualTo("Address");
        assertThat(entity.getCuisineType()).isEqualTo("Brazilian");
        assertThat(entity.getOperatingHours()).isEqualTo("10:00-22:00");
        assertThat(entity.getOwner().getId()).isEqualTo(1L);
        assertThat(entity.getOwner().getLogin()).isEqualTo("owner");
        assertThat(entity.getCreatedAt()).isEqualTo(now);
        assertThat(entity.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void toPersistence_whenNull_returnsNull() {
        assertThat(mapper.toPersistence(null)).isNull();
    }
}
