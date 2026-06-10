package br.com.tech.challenger.api_restaurante.domain.repository;

import br.com.tech.challenger.api_restaurante.domain.entity.UserType;

import java.util.List;
import java.util.Optional;

public interface UserTypeRepository {

    UserType save(UserType userType);

    Optional<UserType> findById(Long id);

    Optional<UserType> findByName(String name);

    List<UserType> findAll();

    boolean existsByName(String name);

    void deleteById(Long id);
}