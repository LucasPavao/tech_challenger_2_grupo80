package br.com.tech.challenger.api_restaurante.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {

    private Long id;

    private Restaurant restaurant;

    private String name;
    private String description;

    private BigDecimal price;

    private Boolean availableOnlyAtLocation;

    private String imagePath;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}