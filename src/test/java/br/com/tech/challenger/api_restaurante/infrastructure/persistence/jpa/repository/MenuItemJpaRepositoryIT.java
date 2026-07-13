package br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.repository;

import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.MenuItemJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.RestaurantJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserAddressJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserTypeJpaEntity;
import br.com.tech.challenger.api_restaurante.support.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MenuItemJpaRepositoryIT extends AbstractIntegrationTest {

    @Autowired
    private MenuItemJpaRepository repository;

    @Autowired
    private RestaurantJpaRepository restaurantRepository;

    @Autowired
    private UserJpaRepository userRepository;

    @Autowired
    private UserTypeJpaRepository userTypeRepository;

    private RestaurantJpaEntity restaurant;

    @BeforeEach
    void setUp() {
        UserTypeJpaEntity userType = userTypeRepository.save(UserTypeJpaEntity.builder().name("OWNER_IT").build());

        UserAddressJpaEntity address = UserAddressJpaEntity.builder()
                .street("Rua Teste")
                .number("100")
                .city("São Paulo")
                .state("SP")
                .zipCode("00000-000")
                .country("Brasil")
                .build();

        UserJpaEntity owner = userRepository.save(UserJpaEntity.builder()
                .name("Owner")
                .email("owner@example.com")
                .login("owner")
                .password("senha-hash")
                .userType(userType)
                .userAddress(address)
                .build());

        restaurant = restaurantRepository.save(RestaurantJpaEntity.builder()
                .name("Test Restaurant")
                .address("Address")
                .cuisineType("Brazilian")
                .operatingHours("10:00-22:00")
                .owner(owner)
                .build());
    }

    private MenuItemJpaEntity newMenuItem(String name) {
        return MenuItemJpaEntity.builder()
                .name(name)
                .description("Description")
                .price(BigDecimal.valueOf(15.20))
                .availableOnlyAtLocation(false)
                .restaurant(restaurant)
                .build();
    }

    @Test
    void save_thenFindById_returnsPersistedEntityWithRestaurant() {
        MenuItemJpaEntity saved = repository.save(newMenuItem("Rice and Beans"));

        Optional<MenuItemJpaEntity> found = repository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Rice and Beans");
        assertThat(found.get().getRestaurant().getId()).isEqualTo(restaurant.getId());
        assertThat(found.get().getCreatedAt()).isNotNull();
    }

    @Test
    void findByRestaurantId_returnsOnlyItemsFromThatRestaurant() {
        repository.save(newMenuItem("Rice and Beans"));
        repository.save(newMenuItem("Feijoada"));

        List<MenuItemJpaEntity> found = repository.findByRestaurantId(restaurant.getId());

        assertThat(found).hasSize(2);
        assertThat(found).extracting(MenuItemJpaEntity::getName)
                .containsExactlyInAnyOrder("Rice and Beans", "Feijoada");
    }

    @Test
    void findByRestaurantId_whenNoItems_returnsEmpty() {
        assertThat(repository.findByRestaurantId(restaurant.getId())).isEmpty();
    }

    @Test
    void deleteById_removesEntity() {
        MenuItemJpaEntity saved = repository.save(newMenuItem("Rice and Beans"));

        repository.deleteById(saved.getId());

        assertThat(repository.findById(saved.getId())).isEmpty();
    }
}
