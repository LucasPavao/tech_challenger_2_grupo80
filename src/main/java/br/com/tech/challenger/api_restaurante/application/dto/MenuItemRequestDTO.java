package br.com.tech.challenger.api_restaurante.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MenuItemRequestDTO(

        @Schema(description = "Menu item name", example = "Cheeseburger", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Name is required")
        String name,

        @Schema(description = "Menu item description", example = "Beef patty with cheddar and special sauce")
        String description,

        @Schema(description = "Menu item price", example = "29.90", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", message = "Price must be greater than zero")
        BigDecimal price,

        @Schema(description = "Whether the item is only available at the physical location", example = "false")
        Boolean availableOnlyAtLocation,

        @Schema(description = "Path/URL of the menu item image", example = "/images/cheeseburger.png")
        String imagePath,

        @Schema(description = "ID of the restaurant this menu item belongs to", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Menu item must be registered with a restaurant")
        Long restaurantId
) {
}