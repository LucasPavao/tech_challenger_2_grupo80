package br.com.tech.challenger.api_restaurante.application.usecase.user;

import br.com.tech.challenger.api_restaurante.application.dto.UserAddressDTO;
import br.com.tech.challenger.api_restaurante.application.dto.UserDTO;
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

    public UserDTO create(UserDTO dto) {
        if (repository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (repository.existsByLogin(dto.login())) {
            throw new IllegalArgumentException("Login already exists");
        }

        UserType userType = userTypeRepository.findById(dto.userTypeId())
                .orElseThrow(() -> new RuntimeException("User type not found"));

        UserAddress userAddress = buildUserAddressFromDTO(dto.userAddress());

        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .login(dto.login())
                .password(dto.password())
                .userType(userType)
                .userAddress(userAddress)
                .build();

        User saved = repository.save(user);
        return mapToDTO(saved);
    }

    public List<UserDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public UserDTO findById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDTO(user);
    }

    public List<UserDTO> findByName(String name) {
        return repository.findByNameContaining(name)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public UserDTO findByEmail(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDTO(user);
    }

    public UserDTO findByLogin(String login) {
        User user = repository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDTO(user);
    }

    public UserDTO update(Long id, UserDTO dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getEmail().equals(dto.email()) && repository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (!user.getLogin().equals(dto.login()) && repository.existsByLogin(dto.login())) {
            throw new IllegalArgumentException("Login already exists");
        }

        UserType userType = userTypeRepository.findById(dto.userTypeId())
                .orElseThrow(() -> new RuntimeException("User type not found"));

        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setLogin(dto.login());
        user.setPassword(dto.password());
        user.setUserType(userType);
        user.setUserAddress(buildUserAddressForUpdate(user.getUserAddress(), dto.userAddress()));

        User updated = repository.save(user);
        return mapToDTO(updated);
    }

    public void delete(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        repository.deleteById(id);
    }

    private UserDTO mapToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                null,
                user.getUserType().getId(),
                mapUserAddressToDTO(user.getUserAddress())
        );
    }

    private UserAddressDTO mapUserAddressToDTO(UserAddress address) {
        if (address == null) {
            return null;
        }
        return new UserAddressDTO(
                address.getId(),
                address.getStreet(),
                address.getCity(),
                address.getNumber(),
                address.getComplement(),
                address.getState(),
                address.getZipCode(),
                address.getCountry()
        );
    }

    private UserAddress buildUserAddressFromDTO(UserAddressDTO dto) {
        if (dto == null) {
            return null;
        }
        return UserAddress.builder()
                .id(dto.id())
                .street(dto.street())
                .city(dto.city())
                .number(dto.number())
                .complement(dto.complement())
                .state(dto.state())
                .zipCode(dto.zipCode())
                .country(dto.country())
                .build();
    }

    private UserAddress buildUserAddressForUpdate(UserAddress existing, UserAddressDTO dto) {
        if (dto == null) {
            return existing;
        }

        if (existing == null) {
            return UserAddress.builder()
                    .street(dto.street())
                    .city(dto.city())
                    .number(dto.number())
                    .complement(dto.complement())
                    .state(dto.state())
                    .zipCode(dto.zipCode())
                    .country(dto.country())
                    .build();
        }

        existing.setStreet(dto.street());
        existing.setCity(dto.city());
        existing.setNumber(dto.number());
        existing.setComplement(dto.complement());
        existing.setState(dto.state());
        existing.setZipCode(dto.zipCode());
        existing.setCountry(dto.country());

        return existing;
    }
}

