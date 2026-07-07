package br.com.tech.challenger.api_restaurante.application.usecase.menuitem;

import br.com.tech.challenger.api_restaurante.application.dto.MenuItemDTO;
import br.com.tech.challenger.api_restaurante.application.dto.MenuItemRequestDTO;
import br.com.tech.challenger.api_restaurante.application.mapper.MenuItemDtoMapper;
import br.com.tech.challenger.api_restaurante.domain.entity.MenuItem;
import br.com.tech.challenger.api_restaurante.domain.entity.Restaurant;
import br.com.tech.challenger.api_restaurante.application.exception.MenuItemNotFoundException;
import br.com.tech.challenger.api_restaurante.application.exception.RestaurantNotFoundException;
import br.com.tech.challenger.api_restaurante.domain.repository.MenuItemRepository;
import br.com.tech.challenger.api_restaurante.domain.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateMenuItemUseCase {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemDtoMapper mapper;

    public MenuItemDTO execute(Long id, MenuItemRequestDTO requestDTO) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu item not found with id: " + id));

        Restaurant restaurant = restaurantRepository.findById(requestDTO.restaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException(
                        "Restaurant not found with id: " + requestDTO.restaurantId()));

        mapper.updateDomainFromRequest(menuItem, requestDTO, restaurant);
        MenuItem updated = menuItemRepository.save(menuItem);

        return mapper.toDTO(updated);
    }
}