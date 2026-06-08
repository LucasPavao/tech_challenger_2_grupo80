package br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.repository;

import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserAddressJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAddressJpaRepository extends JpaRepository<UserAddressJpaEntity, Long> {
}

