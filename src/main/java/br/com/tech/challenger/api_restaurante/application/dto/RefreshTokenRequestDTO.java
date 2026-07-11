package br.com.tech.challenger.api_restaurante.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Refresh token request")
public record RefreshTokenRequestDTO(

        @Schema(description = "Refresh token previously issued at login", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Refresh token is required")
        String refreshToken
) {
}
