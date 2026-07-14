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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateUserTypeUseCaseTest {

    @Mock
    private UserTypeRepository repository;

    private UpdateUserTypeUseCase useCase;

    private UserType exampleDomain;

    @BeforeEach
    void setUp() {
        useCase = new UpdateUserTypeUseCase(repository);

        exampleDomain = UserType.builder()
                .id(1L)
                .name("Admin")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void execute_success() {
        when(repository.findById(1L)).thenReturn(Optional.of(exampleDomain));
        when(repository.save(any(UserType.class))).thenAnswer(inv -> inv.getArgument(0));

        UserTypeDTO dto = new UserTypeDTO(null, "Customer");

        UserTypeDTO updated = useCase.execute(1L, dto);

        assertThat(updated).isNotNull();
        assertThat(updated.id()).isEqualTo(1L);
        assertThat(updated.name()).isEqualTo("Customer");
    }
}
