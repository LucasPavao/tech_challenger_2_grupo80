package br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.repository;

import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {

    List<UserJpaEntity> findByNameContaining(String name);

    Optional<UserJpaEntity> findByEmail(String email);

    Optional<UserJpaEntity> findByLogin(String login);

    Optional<UserJpaEntity> findByLoginAndPassword(String login, String password);

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);
}

