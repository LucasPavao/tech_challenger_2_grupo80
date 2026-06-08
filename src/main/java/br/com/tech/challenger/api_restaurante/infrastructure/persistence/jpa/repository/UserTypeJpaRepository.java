package br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.repository;

import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserTypeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTypeJpaRepository extends JpaRepository<UserTypeJpaEntity, Long> {

    Optional<UserTypeJpaEntity> findByName(String name);

    boolean existsByName(String name);
}

