package br.com.tech.challenger.api_restaurante.application.mapper;

import br.com.tech.challenger.api_restaurante.application.dto.UserAddressDTO;
import br.com.tech.challenger.api_restaurante.domain.entity.UserAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAddressDtoMapper {

    public UserAddressDTO toDto(UserAddress address) {
        if (address == null) {
            return null;
        }
        return new UserAddressDTO(
                address.getId(),
                address.getStreet(),
                address.getCity(),
                address.getNumber(),
                address.getComplement(),
                address.getState(),
                address.getZipCode(),
                address.getCountry()
        );
    }

    public UserAddress buildUserAddressFromDTO(UserAddressDTO dto) {
        if (dto == null) {
            return null;
        }
        return UserAddress.builder()
                .id(dto.id())
                .street(dto.street())
                .city(dto.city())
                .number(dto.number())
                .complement(dto.complement())
                .state(dto.state())
                .zipCode(dto.zipCode())
                .country(dto.country())
                .build();
    }

    public UserAddress buildUserAddressForUpdate(UserAddress existing, UserAddressDTO dto) {
        if (dto == null) {
            return existing;
        }

        if (existing == null) {
            return UserAddress.builder()
                    .street(dto.street())
                    .city(dto.city())
                    .number(dto.number())
                    .complement(dto.complement())
                    .state(dto.state())
                    .zipCode(dto.zipCode())
                    .country(dto.country())
                    .build();
        }

        existing.setStreet(dto.street());
        existing.setCity(dto.city());
        existing.setNumber(dto.number());
        existing.setComplement(dto.complement());
        existing.setState(dto.state());
        existing.setZipCode(dto.zipCode());
        existing.setCountry(dto.country());

        return existing;
    }
}
