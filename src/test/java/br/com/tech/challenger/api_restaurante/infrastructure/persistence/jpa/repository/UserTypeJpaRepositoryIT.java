package br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.repository;

import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserTypeJpaEntity;
import br.com.tech.challenger.api_restaurante.support.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserTypeJpaRepositoryIT extends AbstractIntegrationTest {

    @Autowired
    private UserTypeJpaRepository repository;

    @Test
    void save_thenFindByName_returnsPersistedEntity() {
        repository.save(UserTypeJpaEntity.builder().name("REGIONAL_MANAGER").build());

        Optional<UserTypeJpaEntity> found = repository.findByName("REGIONAL_MANAGER");

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isNotNull();
        assertThat(found.get().getCreatedAt()).isNotNull();
    }

    @Test
    void existsByName_whenNotPersisted_returnsFalse() {
        assertThat(repository.existsByName("DOES_NOT_EXIST")).isFalse();
    }

    @Test
    void save_withDuplicateName_violatesUniqueConstraint() {
        repository.saveAndFlush(UserTypeJpaEntity.builder().name("DUPLICATED").build());

        assertThatThrownBy(() ->
                repository.saveAndFlush(UserTypeJpaEntity.builder().name("DUPLICATED").build())
        ).isInstanceOf(DataIntegrityViolationException.class);
    }
}
