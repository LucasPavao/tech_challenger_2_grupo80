package br.com.tech.challenger.api_restaurante.application.mapper;

import br.com.tech.challenger.api_restaurante.application.dto.MenuItemDTO;
import br.com.tech.challenger.api_restaurante.application.dto.MenuItemRequestDTO;
import br.com.tech.challenger.api_restaurante.domain.entity.MenuItem;
import br.com.tech.challenger.api_restaurante.domain.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuItemDtoMapper {

    private final RestaurantDtoMapper restaurantDtoMapper;

    public MenuItemDTO toDTO(MenuItem menuItem) {
        return new MenuItemDTO(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.getAvailableOnlyAtLocation(),
                menuItem.getImagePath(),
                restaurantDtoMapper.toDto(menuItem.getRestaurant()),
                menuItem.getCreatedAt(),
                menuItem.getUpdatedAt()
        );
    }


    public MenuItem toDomain(MenuItemRequestDTO requestDTO, Restaurant restaurant) {
        return MenuItem.builder()
                .name(requestDTO.name())
                .description(requestDTO.description())
                .price(requestDTO.price())
                .availableOnlyAtLocation(requestDTO.availableOnlyAtLocation())
                .imagePath(requestDTO.imagePath())
                .restaurant(restaurant)
                .build();
    }


    public void updateDomainFromRequest(MenuItem menuItem, MenuItemRequestDTO requestDTO, Restaurant restaurant) {
        menuItem.setName(requestDTO.name());
        menuItem.setDescription(requestDTO.description());
        menuItem.setPrice(requestDTO.price());
        menuItem.setAvailableOnlyAtLocation(requestDTO.availableOnlyAtLocation());
        menuItem.setImagePath(requestDTO.imagePath());
        menuItem.setRestaurant(restaurant);
    }
}