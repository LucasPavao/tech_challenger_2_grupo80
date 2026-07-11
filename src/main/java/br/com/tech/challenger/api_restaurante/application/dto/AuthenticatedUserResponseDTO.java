package br.com.tech.challenger.api_restaurante.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authenticated user data with issued tokens")
public record AuthenticatedUserResponseDTO(

        @Schema(description = "Authenticated user data")
        UserDTO user,

        @Schema(description = "Issued token pair")
        TokenResponseDTO token
) {
}
