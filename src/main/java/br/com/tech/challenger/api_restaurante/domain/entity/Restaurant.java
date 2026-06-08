package br.com.tech.challenger.api_restaurante.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}