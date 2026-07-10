package br.com.tech.challenger.api_restaurante.application.usecase.user;

import br.com.tech.challenger.api_restaurante.application.dto.UserDTO;
import br.com.tech.challenger.api_restaurante.application.exception.EmailAlreadyExistsException;
import br.com.tech.challenger.api_restaurante.application.exception.LoginAlreadyExistsException;
import br.com.tech.challenger.api_restaurante.application.exception.UserTypeNotFoundException;
import br.com.tech.challenger.api_restaurante.application.mapper.UserAddressDtoMapper;
import br.com.tech.challenger.api_restaurante.application.mapper.UserDtoMapper;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.entity.UserAddress;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.domain.repository.UserRepository;
import br.com.tech.challenger.api_restaurante.domain.repository.UserTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;
    private final UserDtoMapper userDtoMapper;
    private final UserAddressDtoMapper userAddressDtoMapper;

    public UserDTO execute(UserDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        if (userRepository.existsByLogin(dto.login())) {
            throw new LoginAlreadyExistsException("Login already exists");
        }

        UserType userType = userTypeRepository.findById(dto.userTypeId())
                .orElseThrow(() -> new UserTypeNotFoundException("User type not found"));

        UserAddress userAddress = userAddressDtoMapper.buildUserAddressFromDTO(dto.userAddress());

        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .login(dto.login())
                .password(dto.password())
                .userType(userType)
                .userAddress(userAddress)
                .build();

        User saved = userRepository.save(user);
        return userDtoMapper.toDTO(saved);
    }
}
