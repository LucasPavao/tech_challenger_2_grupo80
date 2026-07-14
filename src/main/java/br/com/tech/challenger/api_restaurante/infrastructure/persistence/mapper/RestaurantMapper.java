package br.com.tech.challenger.api_restaurante.infrastructure.persistence.mapper;

import br.com.tech.challenger.api_restaurante.domain.entity.Restaurant;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.RestaurantJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestaurantMapper {

    private final UserMapper userMapper;

    public Restaurant toDomain(RestaurantJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return Restaurant.builder()
                .id(entity.getId())
                .name(entity.getName())
                .address(entity.getAddress())
                .cuisineType(entity.getCuisineType())
                .operatingHours(entity.getOperatingHours())
                .owner(userMapper.toDomain(entity.getOwner()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public RestaurantJpaEntity toPersistence(Restaurant domain) {
        if (domain == null) {
            return null;
        }

        return RestaurantJpaEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .address(domain.getAddress())
                .cuisineType(domain.getCuisineType())
                .operatingHours(domain.getOperatingHours())
                .owner(userMapper.toPersistence(domain.getOwner()))
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}

