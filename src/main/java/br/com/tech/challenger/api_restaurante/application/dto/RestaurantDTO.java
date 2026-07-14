package br.com.tech.challenger.api_restaurante.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Data Transfer Object representing a restaurant")
public record RestaurantDTO(

        @Schema(description = "Unique identifier of the restaurant", example = "1")
        Long id,

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
        String operatingHours,

        @Schema(description = "Restaurant's owner", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Restaurant must be registered with an owner")
        @Valid
        UserDTO owner
) {
}
