package br.com.tech.challenger.api_restaurante.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDTO(
        Long id,
        String name,
        String email,
        String login,
        String password,
        Long userTypeId,
        UserAddressDTO userAddress
) {
}



