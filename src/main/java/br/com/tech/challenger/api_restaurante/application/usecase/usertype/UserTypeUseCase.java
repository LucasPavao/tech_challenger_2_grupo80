package br.com.tech.challenger.api_restaurante.application.usecase.usertype;

import br.com.tech.challenger.api_restaurante.application.dto.UserTypeDTO;
import br.com.tech.challenger.api_restaurante.application.exception.UserTypeNotFoundException;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.domain.repository.UserTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserTypeUseCase {

    private final UserTypeRepository repository;

    public UserTypeDTO create(UserTypeDTO dto) {

        if (repository.existsByName(dto.name())) {
            throw new IllegalArgumentException(
                    "User type already exists"
            );
        }

        UserType userType = UserType.builder()
                .name(dto.name())
                .build();

        UserType saved = repository.save(userType);

        return new UserTypeDTO(
                saved.getId(),
                saved.getName()
        );
    }

    public List<UserTypeDTO> findAll() {

        return repository.findAll()
                .stream()
                .map(userType ->
                        new UserTypeDTO(
                                userType.getId(),
                                userType.getName()))
                .toList();
    }

    public UserTypeDTO findById(Long id) {

        UserType userType = repository.findById(id)
                .orElseThrow(() ->
                        new UserTypeNotFoundException("User type not found"));

        return new UserTypeDTO(
                userType.getId(),
                userType.getName()
        );
    }

    public UserTypeDTO update(Long id, UserTypeDTO dto) {

        UserType userType = repository.findById(id)
                .orElseThrow(() ->
                        new UserTypeNotFoundException("User type not found"));

        userType.setName(dto.name());

        UserType updated = repository.save(userType);

        return new UserTypeDTO(
                updated.getId(),
                updated.getName()
        );
    }

    public void delete(Long id) {

        repository.findById(id)
                .orElseThrow(() ->
                        new UserTypeNotFoundException("User type not found"));

        repository.deleteById(id);
    }

}
