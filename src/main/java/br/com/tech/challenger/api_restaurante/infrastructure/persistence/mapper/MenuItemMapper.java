package br.com.tech.challenger.api_restaurante.infrastructure.persistence.mapper;

import br.com.tech.challenger.api_restaurante.domain.entity.MenuItem;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.MenuItemJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuItemMapper {

    private final RestaurantMapper restaurantMapper;

    public MenuItem toDomain(MenuItemJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return MenuItem.builder()
                .id(entity.getId())
                .restaurant(restaurantMapper.toDomain(entity.getRestaurant()))
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .availableOnlyAtLocation(entity.getAvailableOnlyAtLocation())
                .imagePath(entity.getImagePath())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public MenuItemJpaEntity toPersistence(MenuItem domain) {
        if (domain == null) {
            return null;
        }

        return MenuItemJpaEntity.builder()
                .id(domain.getId())
                .restaurant(restaurantMapper.toPersistence(domain.getRestaurant()))
                .name(domain.getName())
                .description(domain.getDescription())
                .price(domain.getPrice())
                .availableOnlyAtLocation(domain.getAvailableOnlyAtLocation())
                .imagePath(domain.getImagePath())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}

