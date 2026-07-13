package br.com.tech.challenger.api_restaurante.infrastructure.persistence.mapper;

import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserTypeJpaEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserTypeMapperTest {

    private final UserTypeMapper mapper = new UserTypeMapper();

    @Test
    void toDomain_mapsAllFields() {
        LocalDateTime now = LocalDateTime.now();
        UserTypeJpaEntity entity = UserTypeJpaEntity.builder()
                .id(1L)
                .name("ADMIN")
                .createdAt(now)
                .updatedAt(now)
                .build();

        UserType domain = mapper.toDomain(entity);

        assertThat(domain.getId()).isEqualTo(1L);
        assertThat(domain.getName()).isEqualTo("ADMIN");
        assertThat(domain.getCreatedAt()).isEqualTo(now);
        assertThat(domain.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void toDomain_whenNull_returnsNull() {
        assertThat(mapper.toDomain(null)).isNull();
    }

    @Test
    void toPersistence_mapsAllFields() {
        LocalDateTime now = LocalDateTime.now();
        UserType domain = UserType.builder()
                .id(1L)
                .name("ADMIN")
                .createdAt(now)
                .updatedAt(now)
                .build();

        UserTypeJpaEntity entity = mapper.toPersistence(domain);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getName()).isEqualTo("ADMIN");
        assertThat(entity.getCreatedAt()).isEqualTo(now);
        assertThat(entity.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void toPersistence_whenNull_returnsNull() {
        assertThat(mapper.toPersistence(null)).isNull();
    }
}
