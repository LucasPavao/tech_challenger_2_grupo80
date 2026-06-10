package br.com.tech.challenger.api_restaurante.domain.repository;

import br.com.tech.challenger.api_restaurante.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    List<User> findByNameContaining(String name);

    Optional<User> findByEmail(String email);

    Optional<User> findByLogin(String login);

    Optional<User> findByLoginAndPassword(
            String login,
            String password
    );

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);

    void deleteById(Long id);
}