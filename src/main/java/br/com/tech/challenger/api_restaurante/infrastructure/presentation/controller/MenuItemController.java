package br.com.tech.challenger.api_restaurante.infrastructure.presentation.controller;

import br.com.tech.challenger.api_restaurante.application.dto.MenuItemDTO;
import br.com.tech.challenger.api_restaurante.application.dto.MenuItemRequestDTO;
import br.com.tech.challenger.api_restaurante.application.usecase.menuitem.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/menu-items")
@RequiredArgsConstructor
@Tag(name = "Menu Items", description = "Endpoints for managing restaurant menu items")
public class MenuItemController {

    private final CreateMenuItemUseCase createMenuItemUseCase;
    private final FindMenuItemUseCase findMenuItemUseCase;
    private final FindMenuItemByIdUseCase findMenuItemByIdUseCase;
    private final UpdateMenuItemUseCase updateMenuItemUseCase;
    private final DeleteMenuItemUseCase deleteMenuItemUseCase;

    @PostMapping
    @Operation(summary = "Create a new menu item")
    public ResponseEntity<MenuItemDTO> create(@Valid @RequestBody MenuItemRequestDTO requestDTO) {
        MenuItemDTO created = createMenuItemUseCase.execute(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "List all menu items, optionally filtered by restaurant")
    public ResponseEntity<List<MenuItemDTO>> findAll(
            @RequestParam(required = false) Long restaurantId) {
        if (restaurantId != null) {
            return ResponseEntity.ok(findMenuItemUseCase.executeByRestaurantId(restaurantId));
        }
        return ResponseEntity.ok(findMenuItemUseCase.execute());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a menu item by id")
    public ResponseEntity<MenuItemDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(findMenuItemByIdUseCase.execute(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing menu item")
    public ResponseEntity<MenuItemDTO> update(@PathVariable Long id,
                                              @Valid @RequestBody MenuItemRequestDTO requestDTO) {
        return ResponseEntity.ok(updateMenuItemUseCase.execute(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a menu item")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteMenuItemUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}