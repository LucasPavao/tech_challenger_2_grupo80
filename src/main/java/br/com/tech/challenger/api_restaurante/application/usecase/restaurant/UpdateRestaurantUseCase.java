package br.com.tech.challenger.api_restaurante.application.usecase.restaurant;

import br.com.tech.challenger.api_restaurante.application.dto.RestaurantRequestDTO;
import br.com.tech.challenger.api_restaurante.application.exception.RestaurantNotFoundException;
import br.com.tech.challenger.api_restaurante.application.exception.UserNotAnOwnerException;
import br.com.tech.challenger.api_restaurante.application.exception.UserNotFoundException;
import br.com.tech.challenger.api_restaurante.domain.entity.Restaurant;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.repository.RestaurantRepository;
import br.com.tech.challenger.api_restaurante.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UpdateRestaurantUseCase {
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public Restaurant execute(Long id, RestaurantRequestDTO dto) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));

        User owner = resolveNewOwner(dto.ownerId(), restaurant);

        restaurant.update(
                dto.name(),
                dto.address(),
                dto.cuisineType(),
                dto.operatingHours(),
                owner
        );

        return restaurantRepository.save(restaurant);
    }

    private User resolveNewOwner(Long ownerId, Restaurant restaurant) {
        if (Objects.equals(restaurant.getOwner().getId(), ownerId)) {
            return restaurant.getOwner();
        }

        User newOwner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!newOwner.canOwnRestaurant()) {
            throw new UserNotAnOwnerException(String.format("User %s is not a restaurant owner.", newOwner.getLogin()));
        }

        return newOwner;
    }
}
