package br.com.tech.challenger.api_restaurante.infrastructure.persistence.gateway;

import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.entity.UserAddress;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserAddressJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserTypeJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.repository.UserJpaRepository;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.mapper.UserAddressMapper;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.mapper.UserMapper;
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
class UserGatewayTest {

    @Mock
    private UserJpaRepository jpaRepository;

    private UserGateway gateway;

    private UserJpaEntity exampleEntity;

    @BeforeEach
    void setUp() {
        gateway = new UserGateway(jpaRepository, new UserMapper(new UserTypeMapper(), new UserAddressMapper()));

        LocalDateTime now = LocalDateTime.now();

        UserTypeJpaEntity userTypeEntity = UserTypeJpaEntity.builder().id(1L).name("ADMIN").createdAt(now).updatedAt(now).build();
        UserAddressJpaEntity addressEntity = UserAddressJpaEntity.builder()
                .id(1L).street("Rua Teste").number("100").city("São Paulo").state("SP")
                .zipCode("00000-000").country("Brasil").createdAt(now).updatedAt(now).build();

        exampleEntity = UserJpaEntity.builder()
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
    }

    private User newDomainUser() {
        return User.builder()
                .name("User")
                .email("teste@email.com")
                .login("login")
                .password("password")
                .userType(UserType.builder().id(1L).name("ADMIN").build())
                .userAddress(UserAddress.builder().street("Rua Teste").number("100").city("São Paulo")
                        .state("SP").zipCode("00000-000").country("Brasil").build())
                .build();
    }

    @Test
    void save_delegatesToJpaRepositoryAndMapsResult() {
        when(jpaRepository.save(any(UserJpaEntity.class))).thenReturn(exampleEntity);

        User result = gateway.save(newDomainUser());

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("teste@email.com");
        assertThat(result.getUserType().getName()).isEqualTo("ADMIN");
        assertThat(result.getUserAddress().getStreet()).isEqualTo("Rua Teste");
        verify(jpaRepository).save(any(UserJpaEntity.class));
    }

    @Test
    void findById_whenFound_returnsMappedDomain() {
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(exampleEntity));

        Optional<User> result = gateway.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getLogin()).isEqualTo("login");
    }

    @Test
    void findById_whenNotFound_returnsEmpty() {
        when(jpaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThat(gateway.findById(99L)).isEmpty();
    }

    @Test
    void findAll_returnsMappedList() {
        when(jpaRepository.findAll()).thenReturn(List.of(exampleEntity));

        List<User> result = gateway.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("teste@email.com");
    }

    @Test
    void findByNameContaining_returnsMappedList() {
        when(jpaRepository.findByNameContaining("User")).thenReturn(List.of(exampleEntity));

        List<User> result = gateway.findByNameContaining("User");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("User");
    }

    @Test
    void findByEmail_whenFound_returnsMappedDomain() {
        when(jpaRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(exampleEntity));

        Optional<User> result = gateway.findByEmail("teste@email.com");

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }

    @Test
    void findByEmail_whenNotFound_returnsEmpty() {
        when(jpaRepository.findByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        assertThat(gateway.findByEmail("naoexiste@email.com")).isEmpty();
    }

    @Test
    void findByLogin_whenFound_returnsMappedDomain() {
        when(jpaRepository.findByLogin("login")).thenReturn(Optional.of(exampleEntity));

        Optional<User> result = gateway.findByLogin("login");

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }

    @Test
    void findByLogin_whenNotFound_returnsEmpty() {
        when(jpaRepository.findByLogin("naoexiste")).thenReturn(Optional.empty());

        assertThat(gateway.findByLogin("naoexiste")).isEmpty();
    }

    @Test
    void findByLoginAndPassword_whenFound_returnsMappedDomain() {
        when(jpaRepository.findByLoginAndPassword("login", "password")).thenReturn(Optional.of(exampleEntity));

        Optional<User> result = gateway.findByLoginAndPassword("login", "password");

        assertThat(result).isPresent();
        assertThat(result.get().getLogin()).isEqualTo("login");
    }

    @Test
    void findByLoginAndPassword_whenNotFound_returnsEmpty() {
        when(jpaRepository.findByLoginAndPassword("login", "errada")).thenReturn(Optional.empty());

        assertThat(gateway.findByLoginAndPassword("login", "errada")).isEmpty();
    }

    @Test
    void existsByEmail_delegatesToJpaRepository() {
        when(jpaRepository.existsByEmail("teste@email.com")).thenReturn(true);

        assertThat(gateway.existsByEmail("teste@email.com")).isTrue();
    }

    @Test
    void existsByLogin_delegatesToJpaRepository() {
        when(jpaRepository.existsByLogin("login")).thenReturn(true);

        assertThat(gateway.existsByLogin("login")).isTrue();
    }

    @Test
    void deleteById_delegatesToJpaRepository() {
        gateway.deleteById(1L);

        verify(jpaRepository).deleteById(1L);
    }
}
