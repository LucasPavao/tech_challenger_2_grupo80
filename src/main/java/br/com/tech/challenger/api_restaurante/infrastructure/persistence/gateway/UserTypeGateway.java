package br.com.tech.challenger.api_restaurante.infrastructure.persistence.gateway;

import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.domain.repository.UserTypeRepository;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserTypeJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.repository.UserTypeJpaRepository;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.mapper.UserTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserTypeGateway implements UserTypeRepository {

    private final UserTypeJpaRepository jpaRepository;
    private final UserTypeMapper mapper;

    @Override
    public UserType save(UserType userType) {
        UserTypeJpaEntity entity = mapper.toPersistence(userType);
        UserTypeJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<UserType> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<UserType> findByName(String name) {
        return jpaRepository.findByName(name)
                .map(mapper::toDomain);
    }

    @Override
    public List<UserType> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}

