package br.com.tech.challenger.api_restaurante.application.usecase.usertype;

import br.com.tech.challenger.api_restaurante.application.exception.UserTypeNotFoundException;
import br.com.tech.challenger.api_restaurante.domain.repository.UserTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteUserTypeUseCase {

    private final UserTypeRepository repository;

    public void execute(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new UserTypeNotFoundException("User type not found"));

        repository.deleteById(id);
    }
}
