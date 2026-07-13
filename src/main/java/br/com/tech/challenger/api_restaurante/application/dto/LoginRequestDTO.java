package br.com.tech.challenger.api_restaurante.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Login credentials")
public record LoginRequestDTO(

        @Schema(description = "User login", example = "john.smith", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Login is required")
        String login,

        @Schema(description = "User password", example = "StrongPass@123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Password is required")
        String password
) {
}
