package br.com.tech.challenger.api_restaurante.application.mapper;

import br.com.tech.challenger.api_restaurante.application.dto.UserDTO;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDtoMapper {

    private final UserAddressDtoMapper userAddressDtoMapper;

    public UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                null,
                user.getUserType().getId(),
                userAddressDtoMapper.toDto(user.getUserAddress())
        );
    }

}
