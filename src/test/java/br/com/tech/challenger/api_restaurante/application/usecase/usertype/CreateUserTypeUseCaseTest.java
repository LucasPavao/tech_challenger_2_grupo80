package br.com.tech.challenger.api_restaurante.application.usecase.usertype;

import br.com.tech.challenger.api_restaurante.application.dto.UserTypeDTO;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.domain.repository.UserTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUserTypeUseCaseTest {

    @Mock
    private UserTypeRepository repository;

    private CreateUserTypeUseCase useCase;

    private UserType exampleDomain;

    @BeforeEach
    void setUp() {
        useCase = new CreateUserTypeUseCase(repository);

        exampleDomain = UserType.builder()
                .id(1L)
                .name("Admin")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void execute_whenExistsByName_thenThrow() {
        when(repository.existsByName("Admin")).thenReturn(true);

        UserTypeDTO dto = new UserTypeDTO(null, "Admin");

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User type already exists");

        verify(repository, never()).save(any());
    }

    @Test
    void execute_success() {
        when(repository.existsByName("Admin")).thenReturn(false);
        when(repository.save(any(UserType.class))).thenReturn(exampleDomain);

        UserTypeDTO dto = new UserTypeDTO(null, "Admin");

        UserTypeDTO result = useCase.execute(dto);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Admin");

        verify(repository).save(any(UserType.class));
    }
}
