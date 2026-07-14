package br.com.tech.challenger.api_restaurante.application.usecase.user;

import br.com.tech.challenger.api_restaurante.application.dto.UserDTO;
import br.com.tech.challenger.api_restaurante.application.exception.EmailAlreadyExistsException;
import br.com.tech.challenger.api_restaurante.application.exception.LoginAlreadyExistsException;
import br.com.tech.challenger.api_restaurante.application.exception.UserNotFoundException;
import br.com.tech.challenger.api_restaurante.application.mapper.UserAddressDtoMapper;
import br.com.tech.challenger.api_restaurante.application.mapper.UserDtoMapper;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.domain.repository.UserRepository;
import br.com.tech.challenger.api_restaurante.domain.repository.UserTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateUserUseCase {

    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;
    private final UserDtoMapper userDtoMapper;
    private final UserAddressDtoMapper userAddressDtoMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDTO execute(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.getEmail().equals(dto.email()) && userRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        if (!user.getLogin().equals(dto.login()) && userRepository.existsByLogin(dto.login())) {
            throw new LoginAlreadyExistsException("Login already exists");
        }

        UserType userType = userTypeRepository.findById(dto.userTypeId())
                .orElseThrow(() -> new UserNotFoundException("User type not found"));

        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setLogin(dto.login());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setUserType(userType);
        user.setUserAddress(userAddressDtoMapper.buildUserAddressForUpdate(user.getUserAddress(), dto.userAddress()));

        User updated = userRepository.save(user);
        return userDtoMapper.toDTO(updated);
    }
}
