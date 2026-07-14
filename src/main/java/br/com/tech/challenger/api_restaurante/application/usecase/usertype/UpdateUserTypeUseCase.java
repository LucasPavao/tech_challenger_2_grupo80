package br.com.tech.challenger.api_restaurante.application.usecase.usertype;

import br.com.tech.challenger.api_restaurante.application.dto.UserTypeDTO;
import br.com.tech.challenger.api_restaurante.application.exception.UserTypeNotFoundException;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.domain.repository.UserTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateUserTypeUseCase {

    private final UserTypeRepository repository;

    public UserTypeDTO execute(Long id, UserTypeDTO dto) {
        UserType userType = repository.findById(id)
                .orElseThrow(() -> new UserTypeNotFoundException("User type not found"));

        userType.setName(dto.name());

        UserType updated = repository.save(userType);

        return new UserTypeDTO(updated.getId(), updated.getName());
    }
}
