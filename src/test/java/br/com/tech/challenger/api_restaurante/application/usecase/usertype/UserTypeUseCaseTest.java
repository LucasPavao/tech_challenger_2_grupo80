package br.com.tech.challenger.api_restaurante.application.usecase.usertype;

import br.com.tech.challenger.api_restaurante.application.dto.UserTypeDTO;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
import br.com.tech.challenger.api_restaurante.domain.repository.UserTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserTypeUseCaseTest {

    @Mock
    private UserTypeRepository repository;

    @InjectMocks
    private UserTypeUseCase useCase;

    private UserType exampleDomain;

    @BeforeEach
    void setUp() {
        exampleDomain = UserType.builder()
                .id(1L)
                .name("Admin")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void create_whenExistsByName_thenThrow() {
        when(repository.existsByName("Admin")).thenReturn(true);

        UserTypeDTO dto = new UserTypeDTO(null, "Admin");

        assertThatThrownBy(() -> useCase.create(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User type already exists");

        verify(repository, never()).save(any());
    }

    @Test
    void create_success() {
        when(repository.existsByName("Admin")).thenReturn(false);
        when(repository.save(any(UserType.class))).thenReturn(exampleDomain);

        UserTypeDTO dto = new UserTypeDTO(null, "Admin");

        UserTypeDTO result = useCase.create(dto);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Admin");

        verify(repository).save(any(UserType.class));
    }

    @Test
    void findAll_mapsCorrectly() {
        when(repository.findAll()).thenReturn(List.of(exampleDomain));

        List<UserTypeDTO> list = useCase.findAll();

        assertThat(list).hasSize(1);
        assertThat(list.get(0).id()).isEqualTo(1L);
        assertThat(list.get(0).name()).isEqualTo("Admin");
    }

    @Test
    void findById_notFound_thenThrow() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.findById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User type not found");
    }

    @Test
    void update_success() {
        when(repository.findById(1L)).thenReturn(Optional.of(exampleDomain));
        when(repository.save(any(UserType.class))).thenAnswer(inv -> inv.getArgument(0));

        UserTypeDTO dto = new UserTypeDTO(null, "Customer");

        UserTypeDTO updated = useCase.update(1L, dto);

        assertThat(updated).isNotNull();
        assertThat(updated.id()).isEqualTo(1L);
        assertThat(updated.name()).isEqualTo("Customer");
    }

    @Test
    void delete_notFound_thenThrow() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.delete(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User type not found");
    }
}

