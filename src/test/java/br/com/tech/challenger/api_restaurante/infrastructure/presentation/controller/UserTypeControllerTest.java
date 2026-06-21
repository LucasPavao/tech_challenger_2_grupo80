package br.com.tech.challenger.api_restaurante.infrastructure.presentation.controller;

import br.com.tech.challenger.api_restaurante.application.dto.UserTypeDTO;
import br.com.tech.challenger.api_restaurante.application.usecase.usertype.UserTypeUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserTypeControllerTest {

    private MockMvc mockMvc;
    private UserTypeUseCase useCase;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        useCase = Mockito.mock(UserTypeUseCase.class);
        UserTypeController controller = new UserTypeController(useCase);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAll_shouldReturnList() throws Exception {
        when(useCase.findAll()).thenReturn(List.of(new UserTypeDTO(1L, "Admin")));

        mockMvc.perform(get("/v1/user-types"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Admin"));
    }

    @Test
    void create_shouldReturnCreated() throws Exception {
        UserTypeDTO request = new UserTypeDTO(null, "Admin");
        UserTypeDTO response = new UserTypeDTO(1L, "Admin");

        when(useCase.create(any(UserTypeDTO.class))).thenReturn(response);

        mockMvc.perform(post("/v1/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Admin"));
    }

    @Test
    void findById_shouldReturnDto() throws Exception {
        when(useCase.findById(1L)).thenReturn(new UserTypeDTO(1L, "Admin"));

        mockMvc.perform(get("/v1/user-types/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Admin"));
    }

    @Test
    void update_shouldReturnUpdated() throws Exception {
        UserTypeDTO request = new UserTypeDTO(null, "Customer");
        UserTypeDTO response = new UserTypeDTO(1L, "Customer");

        when(useCase.update(any(Long.class), any(UserTypeDTO.class))).thenReturn(response);

        mockMvc.perform(put("/v1/user-types/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Customer"));
    }

    @Test
    void delete_shouldReturnNoContent() throws Exception {
        doNothing().when(useCase).delete(1L);

        mockMvc.perform(delete("/v1/user-types/1"))
                .andExpect(status().isNoContent());
    }
}

