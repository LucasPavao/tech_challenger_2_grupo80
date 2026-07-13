package br.com.tech.challenger.api_restaurante.application.usecase.usertype;

import br.com.tech.challenger.api_restaurante.application.dto.UserTypeDTO;
import br.com.tech.challenger.api_restaurante.domain.repository.UserTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindUserTypeUseCase {

    private final UserTypeRepository repository;

    public List<UserTypeDTO> execute() {
        return repository.findAll()
                .stream()
                .map(userType -> new UserTypeDTO(userType.getId(), userType.getName()))
                .toList();
    }
}
