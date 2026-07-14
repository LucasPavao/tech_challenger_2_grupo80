package br.com.tech.challenger.api_restaurante.domain.repository;

import br.com.tech.challenger.api_restaurante.domain.entity.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository {

    Restaurant save(Restaurant restaurant);

    Optional<Restaurant> findById(Long id);

    List<Restaurant> findAll();

    List<Restaurant> findByOwnerId(Long ownerId);

    List<Restaurant> findByNameContaining(String name);

    void deleteById(Long id);
}