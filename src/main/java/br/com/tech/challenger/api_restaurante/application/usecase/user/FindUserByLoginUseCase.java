package br.com.tech.challenger.api_restaurante.application.usecase.user;

import br.com.tech.challenger.api_restaurante.application.dto.UserDTO;
import br.com.tech.challenger.api_restaurante.application.exception.UserNotFoundException;
import br.com.tech.challenger.api_restaurante.application.mapper.UserDtoMapper;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindUserByLoginUseCase {

    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    public UserDTO execute(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return userDtoMapper.toDTO(user);
    }
}
