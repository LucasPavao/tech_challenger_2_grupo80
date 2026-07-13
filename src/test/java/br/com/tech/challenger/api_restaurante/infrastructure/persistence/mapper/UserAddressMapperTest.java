package br.com.tech.challenger.api_restaurante.infrastructure.persistence.mapper;

import br.com.tech.challenger.api_restaurante.domain.entity.UserAddress;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserAddressJpaEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserAddressMapperTest {

    private final UserAddressMapper mapper = new UserAddressMapper();

    @Test
    void toDomain_mapsAllFields() {
        LocalDateTime now = LocalDateTime.now();
        UserAddressJpaEntity entity = UserAddressJpaEntity.builder()
                .id(1L)
                .street("Rua Teste")
                .number("100")
                .city("São Paulo")
                .state("SP")
                .zipCode("00000-000")
                .complement("Apto 1")
                .country("Brasil")
                .createdAt(now)
                .updatedAt(now)
                .build();

        UserAddress domain = mapper.toDomain(entity);

        assertThat(domain.getId()).isEqualTo(1L);
        assertThat(domain.getStreet()).isEqualTo("Rua Teste");
        assertThat(domain.getNumber()).isEqualTo("100");
        assertThat(domain.getCity()).isEqualTo("São Paulo");
        assertThat(domain.getState()).isEqualTo("SP");
        assertThat(domain.getZipCode()).isEqualTo("00000-000");
        assertThat(domain.getComplement()).isEqualTo("Apto 1");
        assertThat(domain.getCountry()).isEqualTo("Brasil");
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
        UserAddress domain = UserAddress.builder()
                .id(1L)
                .street("Rua Teste")
                .number("100")
                .city("São Paulo")
                .state("SP")
                .zipCode("00000-000")
                .complement("Apto 1")
                .country("Brasil")
                .createdAt(now)
                .updatedAt(now)
                .build();

        UserAddressJpaEntity entity = mapper.toPersistence(domain);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getStreet()).isEqualTo("Rua Teste");
        assertThat(entity.getNumber()).isEqualTo("100");
        assertThat(entity.getCity()).isEqualTo("São Paulo");
        assertThat(entity.getState()).isEqualTo("SP");
        assertThat(entity.getZipCode()).isEqualTo("00000-000");
        assertThat(entity.getComplement()).isEqualTo("Apto 1");
        assertThat(entity.getCountry()).isEqualTo("Brasil");
        assertThat(entity.getCreatedAt()).isEqualTo(now);
        assertThat(entity.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void toPersistence_whenNull_returnsNull() {
        assertThat(mapper.toPersistence(null)).isNull();
    }
}
