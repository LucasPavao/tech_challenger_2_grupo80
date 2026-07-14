package br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.repository;

import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserAddressJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserTypeJpaEntity;
import br.com.tech.challenger.api_restaurante.support.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserJpaRepositoryIT extends AbstractIntegrationTest {

    @Autowired
    private UserJpaRepository repository;

    @Autowired
    private UserTypeJpaRepository userTypeRepository;

    private UserTypeJpaEntity userType;

    @BeforeEach
    void setUp() {
        userType = userTypeRepository.save(UserTypeJpaEntity.builder().name("ADMIN_IT").build());
    }

    private UserJpaEntity newUser(String email, String login) {
        UserAddressJpaEntity address = UserAddressJpaEntity.builder()
                .street("Rua Teste")
                .number("100")
                .city("São Paulo")
                .state("SP")
                .zipCode("00000-000")
                .country("Brasil")
                .build();

        return UserJpaEntity.builder()
                .name("Fulano de Tal")
                .email(email)
                .login(login)
                .password("senha-hash")
                .userType(userType)
                .userAddress(address)
                .build();
    }

    @Test
    void save_thenFindByLogin_returnsPersistedUserWithAssociations() {
        repository.save(newUser("fulano@example.com", "fulano"));

        Optional<UserJpaEntity> found = repository.findByLogin("fulano");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("fulano@example.com");
        assertThat(found.get().getUserType().getId()).isEqualTo(userType.getId());
        assertThat(found.get().getUserAddress().getId()).isNotNull();
    }

    @Test
    void existsByEmail_whenPersisted_returnsTrue() {
        repository.save(newUser("existente@example.com", "existente"));

        assertThat(repository.existsByEmail("existente@example.com")).isTrue();
        assertThat(repository.existsByEmail("ninguem@example.com")).isFalse();
    }

    @Test
    void findByNameContaining_matchesPartialName() {
        repository.save(newUser("busca@example.com", "busca"));

        assertThat(repository.findByNameContaining("Fulano")).isNotEmpty();
    }
}
