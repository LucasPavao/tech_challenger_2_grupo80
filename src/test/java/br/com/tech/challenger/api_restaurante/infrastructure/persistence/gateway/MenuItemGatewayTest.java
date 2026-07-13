package br.com.tech.challenger.api_restaurante.infrastructure.persistence.gateway;

import br.com.tech.challenger.api_restaurante.domain.entity.MenuItem;
import br.com.tech.challenger.api_restaurante.domain.entity.Restaurant;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.MenuItemJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.RestaurantJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserTypeJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.repository.MenuItemJpaRepository;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.mapper.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuItemGatewayTest {

    @Mock
    private MenuItemJpaRepository jpaRepository;

    private MenuItemGateway gateway;

    private MenuItemJpaEntity exampleEntity;

    @BeforeEach
    void setUp() {
        gateway = new MenuItemGateway(jpaRepository, new MenuItemMapper(new RestaurantMapper(new UserMapper(new UserTypeMapper(), new UserAddressMapper()))));

        UserTypeJpaEntity userType = UserTypeJpaEntity.builder().id(1L).name("ADMIN").build();
        UserJpaEntity user = UserJpaEntity.builder().id(1L).email("email").name("User").userType(userType).build();
        RestaurantJpaEntity restaurant = RestaurantJpaEntity.builder().id(1L).name("Test Restaurant").owner(user).build();

        exampleEntity = MenuItemJpaEntity.builder()
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

    private MenuItem newDomainMenuItem() {
        return MenuItem.builder()
                .id(1L)
                .restaurant(Restaurant.builder()
                        .id(1L)
                        .name("Restaurant")
                        .owner(User.builder()
                                .id(1L)
                                .name("User")
                                .userType(UserType.builder()
                                        .id(1L)
                                        .name("OWNER")
                                        .build())
                                .build())
                        .build())
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
    void save_delegatesToJpaRepositoryAndMapsResult() {
        when(jpaRepository.save(any(MenuItemJpaEntity.class))).thenReturn(exampleEntity);

        MenuItem result = gateway.save(newDomainMenuItem());

        assertThat(result.getId()).isEqualTo(exampleEntity.getId());
        assertThat(result.getDescription()).isEqualTo(exampleEntity.getDescription());
        assertThat(result.getPrice()).isEqualTo(exampleEntity.getPrice());
        verify(jpaRepository).save(any(MenuItemJpaEntity.class));
    }

    @Test
    void findById_whenFound_returnsMappedDomain() {
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(exampleEntity));

        Optional<MenuItem> result = gateway.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Rice and Beans");
    }

    @Test
    void findById_whenNotFound_returnsEmpty() {
        when(jpaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThat(gateway.findById(99L)).isEmpty();
    }

    @Test
    void findAll_returnsMappedList() {
        when(jpaRepository.findAll()).thenReturn(List.of(exampleEntity));

        List<MenuItem> result = gateway.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Rice and Beans");
    }

    @Test
    void findByRestaurantId_returnsMappedList() {
        when(jpaRepository.findByRestaurantId(1L)).thenReturn(List.of(exampleEntity));

        List<MenuItem> result = gateway.findByRestaurantId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRestaurant().getId()).isEqualTo(1L);
    }

    @Test
    void deleteById_delegatesToJpaRepository() {
        gateway.deleteById(1L);

        verify(jpaRepository).deleteById(1L);
    }
}
