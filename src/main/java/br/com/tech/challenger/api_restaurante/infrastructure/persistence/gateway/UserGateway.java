package br.com.tech.challenger.api_restaurante.infrastructure.persistence.gateway;

import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.repository.UserRepository;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.entity.UserJpaEntity;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.repository.UserJpaRepository;
import br.com.tech.challenger.api_restaurante.infrastructure.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserGateway implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserMapper mapper;

    @Override
    public User save(User user) {
        UserJpaEntity entity = mapper.toPersistence(user);
        UserJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<User> findByNameContaining(String name) {
        return jpaRepository.findByNameContaining(name)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return jpaRepository.findByLogin(login)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return jpaRepository.findByLoginAndPassword(login, password)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByLogin(String login) {
        return jpaRepository.existsByLogin(login);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}

