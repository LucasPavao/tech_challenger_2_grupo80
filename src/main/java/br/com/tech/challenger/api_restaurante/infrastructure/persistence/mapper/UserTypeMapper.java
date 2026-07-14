package br.com.tech.challenger.api_restaurante.infrastructure.persistence.mapper;

import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserTypeJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UserTypeMapper {

    public UserType toDomain(UserTypeJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return UserType.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public UserTypeJpaEntity toPersistence(UserType domain) {
        if (domain == null) {
            return null;
        }

        return UserTypeJpaEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}

