package br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.repository;

import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.MenuItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemJpaRepository extends JpaRepository<MenuItemJpaEntity, Long> {

    List<MenuItemJpaEntity> findByRestaurantId(Long restaurantId);
}

