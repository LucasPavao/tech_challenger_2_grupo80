package br.com.tech.challenger.api_restaurante.application.usecase.menuitem;

import br.com.tech.challenger.api_restaurante.application.exception.MenuItemNotFoundException;
import br.com.tech.challenger.api_restaurante.domain.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteMenuItemUseCase {

    private final MenuItemRepository menuItemRepository;

    public void execute(Long id) {
        menuItemRepository.findById(id)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu item not found with id: " + id));

        menuItemRepository.deleteById(id);
    }
}