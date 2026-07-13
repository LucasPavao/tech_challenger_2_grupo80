package br.com.tech.challenger.api_restaurante.application.usecase.auth;

import br.com.tech.challenger.api_restaurante.application.dto.RefreshTokenRequestDTO;
import br.com.tech.challenger.api_restaurante.application.dto.TokenResponseDTO;
import br.com.tech.challenger.api_restaurante.application.exception.InvalidTokenException;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.repository.UserRepository;
import br.com.tech.challenger.api_restaurante.infrastructure.security.JwtTokenService;
import br.com.tech.challenger.api_restaurante.infrastructure.security.TokenPair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenUseCase {

    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;

    public TokenResponseDTO execute(RefreshTokenRequestDTO dto) {
        Long userId = Long.valueOf(jwtTokenService.validateRefreshToken(dto.refreshToken()));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidTokenException("User not found"));

        TokenPair tokenPair = jwtTokenService.generateTokenPair(user);

        return new TokenResponseDTO(tokenPair.accessToken(), tokenPair.refreshToken(), tokenPair.expiresAt());
    }
}
