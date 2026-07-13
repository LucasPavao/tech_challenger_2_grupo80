package br.com.tech.challenger.api_restaurante.application.usecase.auth;

import br.com.tech.challenger.api_restaurante.application.dto.AuthenticatedUserResponseDTO;
import br.com.tech.challenger.api_restaurante.application.dto.RegisterRequestDTO;
import br.com.tech.challenger.api_restaurante.application.dto.TokenResponseDTO;
import br.com.tech.challenger.api_restaurante.application.exception.EmailAlreadyExistsException;
import br.com.tech.challenger.api_restaurante.application.exception.InvalidUserTypeException;
import br.com.tech.challenger.api_restaurante.application.exception.LoginAlreadyExistsException;
import br.com.tech.challenger.api_restaurante.application.exception.UserTypeNotFoundException;
import br.com.tech.challenger.api_restaurante.application.mapper.UserAddressDtoMapper;
import br.com.tech.challenger.api_restaurante.application.mapper.UserDtoMapper;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.entity.UserAddress;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.domain.enums.UserTypeEnum;
import br.com.tech.challenger.api_restaurante.domain.repository.UserRepository;
import br.com.tech.challenger.api_restaurante.domain.repository.UserTypeRepository;
import br.com.tech.challenger.api_restaurante.infrastructure.security.JwtTokenService;
import br.com.tech.challenger.api_restaurante.infrastructure.security.TokenPair;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterUseCase {

    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;
    private final UserDtoMapper userDtoMapper;
    private final UserAddressDtoMapper userAddressDtoMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    public AuthenticatedUserResponseDTO execute(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        if (userRepository.existsByLogin(dto.login())) {
            throw new LoginAlreadyExistsException("Login already exists");
        }

        String requestedType = dto.userType() == null || dto.userType().isBlank()
                ? UserTypeEnum.CUSTOMER.name()
                : dto.userType().trim().toUpperCase();

        if (!requestedType.equals(UserTypeEnum.CUSTOMER.name()) && !requestedType.equals(UserTypeEnum.RESTAURANT_OWNER.name())) {
            throw new InvalidUserTypeException("userType must be CUSTOMER or RESTAURANT_OWNER");
        }

        UserType userType = userTypeRepository.findByName(requestedType)
                .orElseThrow(() -> new UserTypeNotFoundException("User type not found"));

        UserAddress userAddress = userAddressDtoMapper.buildUserAddressFromDTO(dto.userAddress());

        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .login(dto.login())
                .password(passwordEncoder.encode(dto.password()))
                .userType(userType)
                .userAddress(userAddress)
                .build();

        User saved = userRepository.save(user);

        TokenPair tokenPair = jwtTokenService.generateTokenPair(saved);
        TokenResponseDTO tokenResponseDTO = new TokenResponseDTO(
                tokenPair.accessToken(), tokenPair.refreshToken(), tokenPair.expiresAt());

        return new AuthenticatedUserResponseDTO(userDtoMapper.toDTO(saved), tokenResponseDTO);
    }
}
