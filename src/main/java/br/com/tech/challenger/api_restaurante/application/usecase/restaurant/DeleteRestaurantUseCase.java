package br.com.tech.challenger.api_restaurante.application.usecase.restaurant;

import br.com.tech.challenger.api_restaurante.application.exception.RestaurantNotFoundException;
import br.com.tech.challenger.api_restaurante.domain.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteRestaurantUseCase {
    private final RestaurantRepository restaurantRepository;

    public void execute(Long id) {
        restaurantRepository.findById(id).orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));
        restaurantRepository.deleteById(id);
    }
}
