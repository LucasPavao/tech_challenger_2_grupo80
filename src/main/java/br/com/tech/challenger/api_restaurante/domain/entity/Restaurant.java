package br.com.tech.challenger.api_restaurante.domain.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    private Long id;

    private String name;
    private String address;
    private String cuisineType;
    private String operatingHours;

    private User owner;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Restaurant create(String name, String address,String cuisineType, String operatingHours, User owner) {
        Restaurant restaurant = new Restaurant();
        restaurant.update(name, address, cuisineType, operatingHours, owner);

        return restaurant;
    }

    public void update(String name, String address, String cuisineType, String operatingHours, User owner) {
        this.name = name;
        this.address = address;
        this.cuisineType = cuisineType;
        this.operatingHours = operatingHours;
        this.owner = owner;
    }

}