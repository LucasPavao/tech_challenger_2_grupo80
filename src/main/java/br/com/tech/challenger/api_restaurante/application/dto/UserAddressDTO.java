package br.com.tech.challenger.api_restaurante.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Data Transfer Object representing a user address")
public record UserAddressDTO(

        @Schema(description = "Unique identifier of the address", example = "1")
        Long id,

        @Schema(description = "Address street name", example = "Paulista Avenue", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Street is required")
        String street,

        @Schema(description = "Address city", example = "Sao Paulo", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "City is required")
        String city,

        @Schema(description = "Address number", example = "1000", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String number,

        @Schema(description = "Address complement", example = "Suite 12", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String complement,

        @Schema(description = "Address state", example = "SP", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "State is required")
        String state,

        @Schema(description = "Address ZIP code", example = "01310-100", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "ZIP code is required")
        String zipCode,

        @Schema(description = "Address country", example = "Brazil", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Country is required")
        String country
) {
}


