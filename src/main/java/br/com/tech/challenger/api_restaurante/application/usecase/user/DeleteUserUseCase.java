package br.com.tech.challenger.api_restaurante.application.usecase.user;

import br.com.tech.challenger.api_restaurante.application.exception.UserNotFoundException;
import br.com.tech.challenger.api_restaurante.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteUserUseCase {

    private final UserRepository userRepository;

    public void execute(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.deleteById(id);
    }
}
