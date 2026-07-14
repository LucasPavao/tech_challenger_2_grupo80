package br.com.tech.challenger.api_restaurante.domain.repository;

import br.com.tech.challenger.api_restaurante.domain.entity.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository {

    MenuItem save(MenuItem menuItem);

    Optional<MenuItem> findById(Long id);

    List<MenuItem> findAll();

    List<MenuItem> findByRestaurantId(Long restaurantId);

    void deleteById(Long id);
}
