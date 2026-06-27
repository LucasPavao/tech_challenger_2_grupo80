package br.com.tech.challenger.api_restaurante.application.usecase.user;

import br.com.tech.challenger.api_restaurante.application.dto.UserDTO;
import br.com.tech.challenger.api_restaurante.application.exception.EmailAlreadyExistsException;
import br.com.tech.challenger.api_restaurante.application.exception.LoginAlreadyExistsException;
import br.com.tech.challenger.api_restaurante.application.exception.UserNotFoundException;
import br.com.tech.challenger.api_restaurante.application.mapper.UserAddressDtoMapper;
import br.com.tech.challenger.api_restaurante.application.mapper.UserDtoMapper;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.entity.UserAddress;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.domain.repository.UserRepository;
import br.com.tech.challenger.api_restaurante.domain.repository.UserTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository repository;
    private final UserTypeRepository userTypeRepository;

    private final UserDtoMapper userDtoMapper;
    private final UserAddressDtoMapper userAddressDtoMapper;

    public UserDTO create(UserDTO dto) {
        if (repository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (repository.existsByLogin(dto.login())) {
            throw new IllegalArgumentException("Login already exists");
        }

        UserType userType = userTypeRepository.findById(dto.userTypeId())
                .orElseThrow(() -> new UserNotFoundException("User type not found"));

        UserAddress userAddress = userAddressDtoMapper.buildUserAddressFromDTO(dto.userAddress());

        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .login(dto.login())
                .password(dto.password())
                .userType(userType)
                .userAddress(userAddress)
                .build();

        User saved = repository.save(user);
        return userDtoMapper.toDTO(saved);
    }

    public List<UserDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(userDtoMapper::toDTO)
                .toList();
    }

    public UserDTO findById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return userDtoMapper.toDTO(user);
    }

    public List<UserDTO> findByName(String name) {
        return repository.findByNameContaining(name)
                .stream()
                .map(userDtoMapper::toDTO)
                .toList();
    }

    public UserDTO findByEmail(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return userDtoMapper.toDTO(user);
    }

    public UserDTO findByLogin(String login) {
        User user = repository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return userDtoMapper.toDTO(user);
    }

    public UserDTO update(Long id, UserDTO dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.getEmail().equals(dto.email()) && repository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        if (!user.getLogin().equals(dto.login()) && repository.existsByLogin(dto.login())) {
            throw new LoginAlreadyExistsException("Login already exists");
        }

        UserType userType = userTypeRepository.findById(dto.userTypeId())
                .orElseThrow(() -> new UserNotFoundException("User type not found"));

        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setLogin(dto.login());
        user.setPassword(dto.password());
        user.setUserType(userType);
        user.setUserAddress(userAddressDtoMapper.buildUserAddressForUpdate(user.getUserAddress(), dto.userAddress()));

        User updated = repository.save(user);
        return userDtoMapper.toDTO(updated);
    }

    public void delete(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        repository.deleteById(id);
    }

}

