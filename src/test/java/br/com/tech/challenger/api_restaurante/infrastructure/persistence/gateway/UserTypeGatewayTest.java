package br.com.tech.challenger.api_restaurante.infrastructure.persistence.gateway;

import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserTypeJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.repository.UserTypeJpaRepository;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.mapper.UserTypeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserTypeGatewayTest {

    @Mock
    private UserTypeJpaRepository jpaRepository;

    private UserTypeGateway gateway;

    private UserTypeJpaEntity exampleEntity;
    private UserType exampleDomain;

    @BeforeEach
    void setUp() {
        gateway = new UserTypeGateway(jpaRepository, new UserTypeMapper());

        LocalDateTime now = LocalDateTime.now();
        exampleEntity = UserTypeJpaEntity.builder().id(1L).name("ADMIN").createdAt(now).updatedAt(now).build();
        exampleDomain = UserType.builder().id(1L).name("ADMIN").createdAt(now).updatedAt(now).build();
    }

    @Test
    void save_delegatesToJpaRepositoryAndMapsResult() {
        when(jpaRepository.save(any(UserTypeJpaEntity.class))).thenReturn(exampleEntity);

        UserType result = gateway.save(UserType.builder().name("ADMIN").build());

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("ADMIN");
        verify(jpaRepository).save(any(UserTypeJpaEntity.class));
    }

    @Test
    void findById_whenFound_returnsMappedDomain() {
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(exampleEntity));

        Optional<UserType> result = gateway.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("ADMIN");
    }

    @Test
    void findById_whenNotFound_returnsEmpty() {
        when(jpaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThat(gateway.findById(99L)).isEmpty();
    }

    @Test
    void findByName_whenFound_returnsMappedDomain() {
        when(jpaRepository.findByName("ADMIN")).thenReturn(Optional.of(exampleEntity));

        Optional<UserType> result = gateway.findByName("ADMIN");

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }

    @Test
    void findAll_returnsMappedList() {
        when(jpaRepository.findAll()).thenReturn(List.of(exampleEntity));

        List<UserType> result = gateway.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("ADMIN");
    }

    @Test
    void existsByName_delegatesToJpaRepository() {
        when(jpaRepository.existsByName("ADMIN")).thenReturn(true);

        assertThat(gateway.existsByName("ADMIN")).isTrue();
    }

    @Test
    void deleteById_delegatesToJpaRepository() {
        gateway.deleteById(1L);

        verify(jpaRepository).deleteById(1L);
    }
}
