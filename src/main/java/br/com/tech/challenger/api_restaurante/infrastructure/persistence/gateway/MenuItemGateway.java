package br.com.tech.challenger.api_restaurante.infrastructure.persistence.gateway;

import br.com.tech.challenger.api_restaurante.domain.entity.MenuItem;
import br.com.tech.challenger.api_restaurante.domain.repository.MenuItemRepository;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.MenuItemJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.repository.MenuItemJpaRepository;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.mapper.MenuItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MenuItemGateway implements MenuItemRepository {

    private final MenuItemJpaRepository jpaRepository;
    private final MenuItemMapper mapper;

    @Override
    public MenuItem save(MenuItem menuItem) {
        MenuItemJpaEntity entity = mapper.toPersistence(menuItem);
        MenuItemJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<MenuItem> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<MenuItem> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<MenuItem> findByRestaurantId(Long restaurantId) {
        return jpaRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}

