package br.com.tech.challenger.api_restaurante.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Public self-registration data. Account type defaults to CUSTOMER when omitted.")
public record RegisterRequestDTO(

        @Schema(description = "User full name", example = "John Smith", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Name is required")
        String name,

        @Schema(description = "User email", example = "john.smith@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email")
        String email,

        @Schema(description = "Unique login for authentication", example = "john.smith", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Login is required")
        String login,

        @Schema(description = "User access password", example = "StrongPass@123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Password is required")
        String password,

        @Schema(description = "User address data", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "User must be registered with an address")
        @Valid
        UserAddressDTO userAddress,

        @Schema(description = "Account type. Defaults to CUSTOMER when omitted. Only CUSTOMER and RESTAURANT_OWNER may be self-assigned.",
                example = "CUSTOMER", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String userType
) {
}
