package br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.repository;

import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.RestaurantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantJpaRepository extends JpaRepository<RestaurantJpaEntity, Long> {

    List<RestaurantJpaEntity> findByOwnerId(Long ownerId);

    List<RestaurantJpaEntity> findByNameContaining(String name);
}

