package br.com.tech.challenger.api_restaurante.infrastructure.persistence.gateway;

import br.com.tech.challenger.api_restaurante.domain.entity.Restaurant;
import br.com.tech.challenger.api_restaurante.domain.repository.RestaurantRepository;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.RestaurantJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.repository.RestaurantJpaRepository;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.mapper.RestaurantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RestaurantGateway implements RestaurantRepository {

    private final RestaurantJpaRepository jpaRepository;
    private final RestaurantMapper mapper;

    @Override
    public Restaurant save(Restaurant restaurant) {
        RestaurantJpaEntity entity = mapper.toPersistence(restaurant);
        RestaurantJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Restaurant> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Restaurant> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Restaurant> findByOwnerId(Long ownerId) {
        return jpaRepository.findByOwnerId(ownerId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Restaurant> findByNameContaining(String name) {
        return jpaRepository.findByNameContaining(name)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}

