package br.com.tech.challenger.api_restaurante.infrastructure.security;

import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserPrincipalTest {

    @Test
    void exposesUsernamePasswordAndAuthorityFromWrappedUser() {
        UserType userType = UserType.builder().id(1L).name("RESTAURANT_OWNER").build();
        User user = User.builder().id(1L).login("john.smith").password("hashed-password").userType(userType).build();

        UserPrincipal principal = new UserPrincipal(user);

        assertThat(principal.getUsername()).isEqualTo("john.smith");
        assertThat(principal.getPassword()).isEqualTo("hashed-password");
        assertThat(principal.getUser()).isSameAs(user);
        assertThat(principal.getAuthorities()).extracting(a -> a.getAuthority())
                .containsExactly("ROLE_RESTAURANT_OWNER");
        assertThat(principal.isAccountNonExpired()).isTrue();
        assertThat(principal.isAccountNonLocked()).isTrue();
        assertThat(principal.isCredentialsNonExpired()).isTrue();
        assertThat(principal.isEnabled()).isTrue();
    }
}
