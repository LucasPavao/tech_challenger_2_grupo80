package br.com.tech.challenger.api_restaurante.application.usecase.menuitem;

import br.com.tech.challenger.api_restaurante.application.dto.MenuItemDTO;
import br.com.tech.challenger.api_restaurante.application.mapper.MenuItemDtoMapper;
import br.com.tech.challenger.api_restaurante.domain.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindMenuItemUseCase {

    private final MenuItemRepository menuItemRepository;
    private final MenuItemDtoMapper mapper;

    public List<MenuItemDTO> execute() {
        return menuItemRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<MenuItemDTO> executeByRestaurantId(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }
}