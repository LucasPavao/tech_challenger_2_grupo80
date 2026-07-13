package br.com.tech.challenger.api_restaurante.infrastructure.presentation.controller;

import br.com.tech.challenger.api_restaurante.application.dto.AuthenticatedUserResponseDTO;
import br.com.tech.challenger.api_restaurante.application.dto.LoginRequestDTO;
import br.com.tech.challenger.api_restaurante.application.dto.RefreshTokenRequestDTO;
import br.com.tech.challenger.api_restaurante.application.dto.RegisterRequestDTO;
import br.com.tech.challenger.api_restaurante.application.dto.TokenResponseDTO;
import br.com.tech.challenger.api_restaurante.application.usecase.auth.LoginUseCase;
import br.com.tech.challenger.api_restaurante.application.usecase.auth.RefreshTokenUseCase;
import br.com.tech.challenger.api_restaurante.application.usecase.auth.RegisterUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Login, registration, and token refresh endpoints")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final RegisterUseCase registerUseCase;

    @Operation(summary = "Authenticate with login and password")
    @PostMapping("/login")
    public ResponseEntity<AuthenticatedUserResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        return ResponseEntity.ok(loginUseCase.execute(dto));
    }

    @Operation(summary = "Exchange a refresh token for a new token pair")
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponseDTO> refreshToken(@RequestBody @Valid RefreshTokenRequestDTO dto) {
        return ResponseEntity.ok(refreshTokenUseCase.execute(dto));
    }

    @Operation(summary = "Register a new account (CUSTOMER by default, or RESTAURANT_OWNER if requested)")
    @PostMapping("/register")
    public ResponseEntity<AuthenticatedUserResponseDTO> register(@RequestBody @Valid RegisterRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registerUseCase.execute(dto));
    }
}
