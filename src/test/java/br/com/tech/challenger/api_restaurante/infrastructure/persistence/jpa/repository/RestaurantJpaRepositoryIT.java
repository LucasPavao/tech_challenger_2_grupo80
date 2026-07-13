package br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.repository;

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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RestaurantJpaRepositoryIT extends AbstractIntegrationTest {

    @Autowired
    private RestaurantJpaRepository repository;

    @Autowired
    private UserJpaRepository userRepository;

    @Autowired
    private UserTypeJpaRepository userTypeRepository;

    private UserJpaEntity owner;

    @BeforeEach
    void setUp() {
        UserTypeJpaEntity userType = userTypeRepository.save(UserTypeJpaEntity.builder().name("OWNER_IT_RESTAURANT").build());

        UserAddressJpaEntity address = UserAddressJpaEntity.builder()
                .street("Rua Teste")
                .number("100")
                .city("São Paulo")
                .state("SP")
                .zipCode("00000-000")
                .country("Brasil")
                .build();

        owner = userRepository.save(UserJpaEntity.builder()
                .name("Owner")
                .email("owner-restaurant@example.com")
                .login("owner-restaurant")
                .password("senha-hash")
                .userType(userType)
                .userAddress(address)
                .build());
    }

    private RestaurantJpaEntity newRestaurant(String name) {
        return RestaurantJpaEntity.builder()
                .name(name)
                .address("Address")
                .cuisineType("Brazilian")
                .operatingHours("10:00-22:00")
                .owner(owner)
                .build();
    }

    @Test
    void save_thenFindById_returnsPersistedEntityWithOwner() {
        RestaurantJpaEntity saved = repository.save(newRestaurant("Restaurant"));

        Optional<RestaurantJpaEntity> found = repository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Restaurant");
        assertThat(found.get().getOwner().getId()).isEqualTo(owner.getId());
        assertThat(found.get().getCreatedAt()).isNotNull();
    }

    @Test
    void findByOwnerId_returnsOnlyRestaurantsFromThatOwner() {
        repository.save(newRestaurant("Restaurant A"));
        repository.save(newRestaurant("Restaurant B"));

        List<RestaurantJpaEntity> found = repository.findByOwnerId(owner.getId());

        assertThat(found).hasSize(2);
        assertThat(found).extracting(RestaurantJpaEntity::getName)
                .containsExactlyInAnyOrder("Restaurant A", "Restaurant B");
    }

    @Test
    void findByNameContaining_matchesPartialName() {
        repository.save(newRestaurant("Cheeseburger House"));

        assertThat(repository.findByNameContaining("Cheeseburger")).isNotEmpty();
    }

    @Test
    void deleteById_removesEntity() {
        RestaurantJpaEntity saved = repository.save(newRestaurant("Restaurant"));

        repository.deleteById(saved.getId());

        assertThat(repository.findById(saved.getId())).isEmpty();
    }
}
