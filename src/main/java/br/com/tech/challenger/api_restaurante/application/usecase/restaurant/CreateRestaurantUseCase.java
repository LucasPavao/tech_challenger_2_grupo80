package br.com.tech.challenger.api_restaurante.application.usecase.restaurant;

import br.com.tech.challenger.api_restaurante.application.dto.RestaurantRequestDTO;
import br.com.tech.challenger.api_restaurante.application.exception.UserNotAnOwnerException;
import br.com.tech.challenger.api_restaurante.application.exception.UserNotFoundException;
import br.com.tech.challenger.api_restaurante.domain.entity.Restaurant;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.repository.RestaurantRepository;
import br.com.tech.challenger.api_restaurante.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateRestaurantUseCase {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public Restaurant execute(RestaurantRequestDTO restaurantDTO) {
        User owner = userRepository.findById(restaurantDTO.ownerId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!owner.canOwnRestaurant()) {
            throw new UserNotAnOwnerException(String.format("User %s is not a restaurant owner.", owner.getLogin()));
        }

        Restaurant restaurant = Restaurant.create(
                restaurantDTO.name(),
                restaurantDTO.address(),
                restaurantDTO.cuisineType(),
                restaurantDTO.operatingHours(),
                owner);

        return restaurantRepository.save(restaurant);
    }
}
