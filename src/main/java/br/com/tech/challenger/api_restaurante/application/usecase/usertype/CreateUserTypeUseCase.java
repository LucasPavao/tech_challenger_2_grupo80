package br.com.tech.challenger.api_restaurante.application.usecase.usertype;

import br.com.tech.challenger.api_restaurante.application.dto.UserTypeDTO;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.domain.repository.UserTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserTypeUseCase {

    private final UserTypeRepository repository;

    public UserTypeDTO execute(UserTypeDTO dto) {
        if (repository.existsByName(dto.name())) {
            throw new IllegalArgumentException("User type already exists");
        }

        UserType userType = UserType.builder()
                .name(dto.name())
                .build();

        UserType saved = repository.save(userType);

        return new UserTypeDTO(saved.getId(), saved.getName());
    }
}
