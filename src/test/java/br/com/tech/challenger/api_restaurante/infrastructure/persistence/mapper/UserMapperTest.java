package br.com.tech.challenger.api_restaurante.infrastructure.persistence.mapper;

import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.entity.UserAddress;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserAddressJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserTypeJpaEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper mapper = new UserMapper(new UserTypeMapper(), new UserAddressMapper());

    @Test
    void toDomain_mapsAllFieldsIncludingAssociations() {
        LocalDateTime now = LocalDateTime.now();

        UserTypeJpaEntity userTypeEntity = UserTypeJpaEntity.builder().id(1L).name("ADMIN").createdAt(now).updatedAt(now).build();
        UserAddressJpaEntity addressEntity = UserAddressJpaEntity.builder()
                .id(1L).street("Rua Teste").number("100").city("São Paulo").state("SP")
                .zipCode("00000-000").country("Brasil").createdAt(now).updatedAt(now).build();

        UserJpaEntity entity = UserJpaEntity.builder()
                .id(1L)
                .name("User")
                .email("teste@email.com")
                .login("login")
                .password("password")
                .userType(userTypeEntity)
                .userAddress(addressEntity)
                .createdAt(now)
                .updatedAt(now)
                .build();

        User domain = mapper.toDomain(entity);

        assertThat(domain.getId()).isEqualTo(1L);
        assertThat(domain.getName()).isEqualTo("User");
        assertThat(domain.getEmail()).isEqualTo("teste@email.com");
        assertThat(domain.getLogin()).isEqualTo("login");
        assertThat(domain.getPassword()).isEqualTo("password");
        assertThat(domain.getUserType().getId()).isEqualTo(1L);
        assertThat(domain.getUserType().getName()).isEqualTo("ADMIN");
        assertThat(domain.getUserAddress().getId()).isEqualTo(1L);
        assertThat(domain.getUserAddress().getStreet()).isEqualTo("Rua Teste");
        assertThat(domain.getCreatedAt()).isEqualTo(now);
        assertThat(domain.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void toDomain_whenNull_returnsNull() {
        assertThat(mapper.toDomain(null)).isNull();
    }

    @Test
    void toPersistence_mapsAllFieldsIncludingAssociations() {
        LocalDateTime now = LocalDateTime.now();

        UserType userType = UserType.builder().id(1L).name("ADMIN").createdAt(now).updatedAt(now).build();
        UserAddress address = UserAddress.builder()
                .id(1L).street("Rua Teste").number("100").city("São Paulo").state("SP")
                .zipCode("00000-000").country("Brasil").createdAt(now).updatedAt(now).build();

        User domain = User.builder()
                .id(1L)
                .name("User")
                .email("teste@email.com")
                .login("login")
                .password("password")
                .userType(userType)
                .userAddress(address)
                .createdAt(now)
                .updatedAt(now)
                .build();

        UserJpaEntity entity = mapper.toPersistence(domain);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getName()).isEqualTo("User");
        assertThat(entity.getEmail()).isEqualTo("teste@email.com");
        assertThat(entity.getLogin()).isEqualTo("login");
        assertThat(entity.getPassword()).isEqualTo("password");
        assertThat(entity.getUserType().getId()).isEqualTo(1L);
        assertThat(entity.getUserType().getName()).isEqualTo("ADMIN");
        assertThat(entity.getUserAddress().getId()).isEqualTo(1L);
        assertThat(entity.getUserAddress().getStreet()).isEqualTo("Rua Teste");
        assertThat(entity.getCreatedAt()).isEqualTo(now);
        assertThat(entity.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void toPersistence_whenNull_returnsNull() {
        assertThat(mapper.toPersistence(null)).isNull();
    }
}
