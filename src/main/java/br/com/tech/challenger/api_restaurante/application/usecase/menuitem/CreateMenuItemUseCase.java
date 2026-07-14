package br.com.tech.challenger.api_restaurante.application.usecase.menuitem;

import br.com.tech.challenger.api_restaurante.application.dto.MenuItemDTO;
import br.com.tech.challenger.api_restaurante.application.dto.MenuItemRequestDTO;
import br.com.tech.challenger.api_restaurante.application.mapper.MenuItemDtoMapper;
import br.com.tech.challenger.api_restaurante.domain.entity.MenuItem;
import br.com.tech.challenger.api_restaurante.domain.entity.Restaurant;
import br.com.tech.challenger.api_restaurante.application.exception.RestaurantNotFoundException;
import br.com.tech.challenger.api_restaurante.domain.repository.MenuItemRepository;
import br.com.tech.challenger.api_restaurante.domain.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateMenuItemUseCase {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemDtoMapper mapper;

    public MenuItemDTO execute(MenuItemRequestDTO requestDTO) {
        Restaurant restaurant = restaurantRepository.findById(requestDTO.restaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException(
                        "Restaurant not found with id: " + requestDTO.restaurantId()));

        MenuItem menuItem = mapper.toDomain(requestDTO, restaurant);
        MenuItem saved = menuItemRepository.save(menuItem);

        return mapper.toDTO(saved);
    }
}