package br.com.tech.challenger.api_restaurante.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserAddressDTO(
        Long id,
        String street,
        String city,
        String number,
        String complement,
        String state,
        String zipCode,
        String country
) {
}


