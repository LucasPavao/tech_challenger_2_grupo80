package br.com.tech.challenger.api_restaurante.application.usecase.user;

import br.com.tech.challenger.api_restaurante.application.dto.UserDTO;
import br.com.tech.challenger.api_restaurante.application.mapper.UserDtoMapper;
import br.com.tech.challenger.api_restaurante.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindUserUseCase {

    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    public List<UserDTO> execute() {
        return userRepository.findAll()
                .stream()
                .map(userDtoMapper::toDTO)
                .toList();
    }

    public List<UserDTO> executeByName(String name) {
        return userRepository.findByNameContaining(name)
                .stream()
                .map(userDtoMapper::toDTO)
                .toList();
    }
}
