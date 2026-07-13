package br.com.tech.challenger.api_restaurante.application.usecase.usertype;

import br.com.tech.challenger.api_restaurante.application.dto.UserTypeDTO;
import br.com.tech.challenger.api_restaurante.application.exception.UserTypeNotFoundException;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.domain.repository.UserTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindUserTypeByIdUseCase {

    private final UserTypeRepository repository;

    public UserTypeDTO execute(Long id) {
        UserType userType = repository.findById(id)
                .orElseThrow(() -> new UserTypeNotFoundException("User type not found"));

        return new UserTypeDTO(userType.getId(), userType.getName());
    }
}
