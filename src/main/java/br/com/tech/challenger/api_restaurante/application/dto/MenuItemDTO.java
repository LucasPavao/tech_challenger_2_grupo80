package br.com.tech.challenger.api_restaurante.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Data Transfer Object representing a menu item")
public record MenuItemDTO(

        @Schema(description = "Unique identifier of the menu item", example = "1")
        Long id,

        @Schema(description = "Menu item name", example = "Cheeseburger", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @Schema(description = "Menu item description", example = "Beef patty with cheddar and special sauce")
        String description,

        @Schema(description = "Menu item price", example = "29.90", requiredMode = Schema.RequiredMode.REQUIRED)
        BigDecimal price,

        @Schema(description = "Whether the item is only available at the physical location", example = "false")
        Boolean availableOnlyAtLocation,

        @Schema(description = "Path/URL of the menu item image", example = "/images/cheeseburger.png")
        String imagePath,

        @Schema(description = "Restaurant this menu item belongs to", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Menu item must be linked to a restaurant")
        @Valid
        RestaurantDTO restaurant,

        @Schema(description = "Creation timestamp")
        LocalDateTime createdAt,

        @Schema(description = "Last update timestamp")
        LocalDateTime updatedAt
) {
}