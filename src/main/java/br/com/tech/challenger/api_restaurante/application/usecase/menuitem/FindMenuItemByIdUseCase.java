package br.com.tech.challenger.api_restaurante.application.usecase.menuitem;

import br.com.tech.challenger.api_restaurante.application.dto.MenuItemDTO;
import br.com.tech.challenger.api_restaurante.application.exception.MenuItemNotFoundException;
import br.com.tech.challenger.api_restaurante.application.mapper.MenuItemDtoMapper;
import br.com.tech.challenger.api_restaurante.domain.entity.MenuItem;
import br.com.tech.challenger.api_restaurante.domain.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindMenuItemByIdUseCase {

    private final MenuItemRepository menuItemRepository;
    private final MenuItemDtoMapper mapper;

    public MenuItemDTO execute(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu item not found with id: " + id));

        return mapper.toDTO(menuItem);
    }
}