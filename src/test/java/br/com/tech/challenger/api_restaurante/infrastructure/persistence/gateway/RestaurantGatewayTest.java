package br.com.tech.challenger.api_restaurante.infrastructure.persistence.gateway;

import br.com.tech.challenger.api_restaurante.domain.entity.Restaurant;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.RestaurantJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserTypeJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.repository.RestaurantJpaRepository;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.mapper.RestaurantMapper;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.mapper.UserAddressMapper;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.mapper.UserMapper;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.mapper.UserTypeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantGatewayTest {

    @Mock
    private RestaurantJpaRepository jpaRepository;

    private RestaurantGateway gateway;

    private RestaurantJpaEntity exampleEntity;

    @BeforeEach
    void setUp() {
        gateway = new RestaurantGateway(jpaRepository, new RestaurantMapper(new UserMapper(new UserTypeMapper(), new UserAddressMapper())));

        UserTypeJpaEntity userType = UserTypeJpaEntity.builder().id(1L).name("RESTAURANT_OWNER").build();
        UserJpaEntity owner = UserJpaEntity.builder().id(1L).name("Owner").email("owner@email.com").login("owner").password("pwd").userType(userType).build();

        exampleEntity = RestaurantJpaEntity.builder()
                .id(1L)
                .name("Restaurant")
                .address("Address")
                .cuisineType("Brazilian")
                .operatingHours("10:00-22:00")
                .owner(owner)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private Restaurant newDomainRestaurant() {
        return Restaurant.builder()
                .name("Restaurant")
                .address("Address")
                .cuisineType("Brazilian")
                .operatingHours("10:00-22:00")
                .owner(User.builder()
                        .id(1L)
                        .name("Owner")
                        .userType(UserType.builder().id(1L).name("RESTAURANT_OWNER").build())
                        .build())
                .build();
    }

    @Test
    void save_delegatesToJpaRepositoryAndMapsResult() {
        when(jpaRepository.save(any(RestaurantJpaEntity.class))).thenReturn(exampleEntity);

        Restaurant result = gateway.save(newDomainRestaurant());

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Restaurant");
        assertThat(result.getOwner().getId()).isEqualTo(1L);
        verify(jpaRepository).save(any(RestaurantJpaEntity.class));
    }

    @Test
    void findById_whenFound_returnsMappedDomain() {
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(exampleEntity));

        Optional<Restaurant> result = gateway.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Restaurant");
    }

    @Test
    void findById_whenNotFound_returnsEmpty() {
        when(jpaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThat(gateway.findById(99L)).isEmpty();
    }

    @Test
    void findAll_returnsMappedList() {
        when(jpaRepository.findAll()).thenReturn(List.of(exampleEntity));

        List<Restaurant> result = gateway.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Restaurant");
    }

    @Test
    void findByOwnerId_returnsMappedList() {
        when(jpaRepository.findByOwnerId(1L)).thenReturn(List.of(exampleEntity));

        List<Restaurant> result = gateway.findByOwnerId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getOwner().getId()).isEqualTo(1L);
    }

    @Test
    void findByNameContaining_returnsMappedList() {
        when(jpaRepository.findByNameContaining("Restaurant")).thenReturn(List.of(exampleEntity));

        List<Restaurant> result = gateway.findByNameContaining("Restaurant");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Restaurant");
    }

    @Test
    void deleteById_delegatesToJpaRepository() {
        gateway.deleteById(1L);

        verify(jpaRepository).deleteById(1L);
    }
}
