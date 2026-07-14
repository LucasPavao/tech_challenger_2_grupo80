package br.com.tech.challenger.api_restaurante.application.mapper;

import br.com.tech.challenger.api_restaurante.application.dto.RestaurantDTO;
import br.com.tech.challenger.api_restaurante.domain.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestaurantDtoMapper {

    private final UserDtoMapper userDtoMapper;

    public RestaurantDTO toDto(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }

        return new RestaurantDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getCuisineType(),
                restaurant.getOperatingHours(),
                userDtoMapper.toDTO(restaurant.getOwner())
        );
    }
}
