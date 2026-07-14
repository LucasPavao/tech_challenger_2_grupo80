package br.com.tech.challenger.api_restaurante.application.usecase.restaurant;

import br.com.tech.challenger.api_restaurante.domain.entity.Restaurant;
import br.com.tech.challenger.api_restaurante.domain.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindRestaurantUseCase {
    private final RestaurantRepository restaurantRepository;

    public List<Restaurant> execute() {
        return restaurantRepository.findAll();
    }
}
