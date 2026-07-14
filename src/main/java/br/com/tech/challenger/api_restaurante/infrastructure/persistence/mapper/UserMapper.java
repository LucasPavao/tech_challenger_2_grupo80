package br.com.tech.challenger.api_restaurante.infrastructure.persistence.mapper;

import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final UserTypeMapper userTypeMapper;
    private final UserAddressMapper userAddressMapper;

    public User toDomain(UserJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .login(entity.getLogin())
                .password(entity.getPassword())
                .userType(userTypeMapper.toDomain(entity.getUserType()))
                .userAddress(userAddressMapper.toDomain(entity.getUserAddress()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public UserJpaEntity toPersistence(User domain) {
        if (domain == null) {
            return null;
        }

        return UserJpaEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .email(domain.getEmail())
                .login(domain.getLogin())
                .password(domain.getPassword())
                .userType(userTypeMapper.toPersistence(domain.getUserType()))
                .userAddress(userAddressMapper.toPersistence(domain.getUserAddress()))
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}

