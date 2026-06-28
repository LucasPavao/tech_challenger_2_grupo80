package br.com.tech.challenger.api_restaurante.domain.entity;

import br.com.tech.challenger.api_restaurante.application.enums.UserTypeEnum;
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
public class User {

    private Long id;

    private String name;
    private String email;
    private String login;
    private String password;

    private UserType userType;
    private UserAddress userAddress;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean canOwnRestaurant() {
        return this.userType != null && this.userType.getName().equals(UserTypeEnum.RESTAURANT_OWNER.name());
    }

}