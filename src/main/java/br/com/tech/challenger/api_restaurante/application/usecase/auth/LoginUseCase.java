package br.com.tech.challenger.api_restaurante.application.usecase.auth;

import br.com.tech.challenger.api_restaurante.application.dto.AuthenticatedUserResponseDTO;
import br.com.tech.challenger.api_restaurante.application.dto.LoginRequestDTO;
import br.com.tech.challenger.api_restaurante.application.dto.TokenResponseDTO;
import br.com.tech.challenger.api_restaurante.application.mapper.UserDtoMapper;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.infrastructure.security.JwtTokenService;
import br.com.tech.challenger.api_restaurante.infrastructure.security.TokenPair;
import br.com.tech.challenger.api_restaurante.infrastructure.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginUseCase {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserDtoMapper userDtoMapper;

    public AuthenticatedUserResponseDTO execute(LoginRequestDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.login(), dto.password()));

        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();

        TokenPair tokenPair = jwtTokenService.generateTokenPair(user);

        TokenResponseDTO tokenResponseDTO = new TokenResponseDTO(
                tokenPair.accessToken(), tokenPair.refreshToken(), tokenPair.expiresAt());

        return new AuthenticatedUserResponseDTO(userDtoMapper.toDTO(user), tokenResponseDTO);
    }
}
