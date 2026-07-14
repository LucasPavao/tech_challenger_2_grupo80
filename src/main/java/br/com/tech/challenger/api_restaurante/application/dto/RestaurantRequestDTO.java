package br.com.tech.challenger.api_restaurante.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RestaurantRequestDTO (

        @Schema(description = "Restaurant name", example = "Mc Donald's", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Name is required")
        String name,

        @Schema(description = "Restaurant address", example = "Paulista Avenue, 1222, São Paulo", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Address is required")
        String address,

        @Schema(description = "Cuisine type", example = "Brazilian", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Cuisine type is required")
        String cuisineType,

        @Schema(description = "Operating hours", example = "10:00AM - 10:00PM", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Operating hours is required")
        String operatingHours
) {
}