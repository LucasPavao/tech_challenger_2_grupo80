package br.com.tech.challenger.api_restaurante.infrastructure.persistence.mapper;

import br.com.tech.challenger.api_restaurante.domain.entity.UserAddress;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserAddressJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UserAddressMapper {

    public UserAddress toDomain(UserAddressJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return UserAddress.builder()
                .id(entity.getId())
                .street(entity.getStreet())
                .number(entity.getNumber())
                .city(entity.getCity())
                .state(entity.getState())
                .zipCode(entity.getZipCode())
                .complement(entity.getComplement())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public UserAddressJpaEntity toPersistence(UserAddress domain) {
        if (domain == null) {
            return null;
        }

        return UserAddressJpaEntity.builder()
                .id(domain.getId())
                .street(domain.getStreet())
                .number(domain.getNumber())
                .city(domain.getCity())
                .state(domain.getState())
                .zipCode(domain.getZipCode())
                .complement(domain.getComplement())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}

