package br.com.tech.challenger.api_restaurante.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object representing a user type/role")
public record UserTypeDTO(

        @Schema(description = "Unique identifier of the user type", example = "1")
        Long id,

        @Schema(description = "Name of the user type", example = "CUSTOMER")
        String name
) {
}