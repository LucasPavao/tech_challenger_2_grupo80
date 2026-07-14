package br.com.tech.challenger.api_restaurante.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Access and refresh token pair")
public record TokenResponseDTO(

        @Schema(description = "JWT access token, sent as 'Authorization: Bearer <token>' on subsequent requests")
        String accessToken,

        @Schema(description = "JWT refresh token, used with POST /v1/auth/refresh-token to obtain a new access token")
        String refreshToken,

        @Schema(description = "Access token expiration timestamp")
        Instant expiresAt
) {
}
