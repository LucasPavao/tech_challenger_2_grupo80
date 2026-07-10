package br.com.tech.challenger.api_restaurante.infrastructure.presentation.controller;

import br.com.tech.challenger.api_restaurante.application.dto.UserAddressDTO;
import br.com.tech.challenger.api_restaurante.application.dto.UserDTO;
import br.com.tech.challenger.api_restaurante.application.exception.EmailAlreadyExistsException;
import br.com.tech.challenger.api_restaurante.application.exception.UserNotFoundException;
import br.com.tech.challenger.api_restaurante.application.usecase.user.UserUseCase;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;
    private UserUseCase useCase;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        useCase = Mockito.mock(UserUseCase.class);
        UserController controller = new UserController(useCase);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAll_shouldReturnList() throws Exception {
        UserAddressDTO exampleAddressDTO = new UserAddressDTO(1L, "Rua Teste", "São Paulo", "100", null, "SP", "00000-000", "Brasil");
        UserDTO exampleDto = new UserDTO(1L, "User", "teste@email.com", "login", "password", 1L, exampleAddressDTO);

        when(useCase.findAll()).thenReturn(List.of(exampleDto));

        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("User"));
    }

    @Test
    void create_shouldReturnCreated() throws Exception {
        UserAddressDTO exampleAddressDTO = new UserAddressDTO(null, "Rua Teste", "São Paulo", "100", null, "SP", "00000-000", "Brasil");
        UserDTO request = new UserDTO(null, "User", "teste@email.com", "login", "password", 1L, exampleAddressDTO);
        UserDTO response = new UserDTO(1L, "User", "teste@email.com", "login", "password", 1L, exampleAddressDTO);

        when(useCase.create(any(UserDTO.class))).thenReturn(response);

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("User"));
    }

    @Test
    void create_whenEmailAlreadyExists_shouldReturnConflict() throws Exception {
        UserAddressDTO exampleAddressDTO = new UserAddressDTO(null, "Rua Teste", "São Paulo", "100", null, "SP", "00000-000", "Brasil");
        UserDTO request = new UserDTO(null, "User", "teste@email.com", "login", "password", 1L, exampleAddressDTO);

        when(useCase.create(any(UserDTO.class))).thenThrow(new EmailAlreadyExistsException("Email already exists"));

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void findById_shouldReturnDto() throws Exception {
        UserAddressDTO exampleAddressDTO = new UserAddressDTO(1L, "Rua Teste", "São Paulo", "100", null, "SP", "00000-000", "Brasil");
        UserDTO exampleDto = new UserDTO(1L, "User", "teste@email.com", "login", "password", 1L, exampleAddressDTO);

        when(useCase.findById(1L)).thenReturn(exampleDto);

        mockMvc.perform(get("/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("User"));
    }

    @Test
    void findById_whenNotFound_shouldReturnBadRequest() throws Exception {
        when(useCase.findById(99L)).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/v1/users/99"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findByName_shouldReturnList() throws Exception {
        UserAddressDTO exampleAddressDTO = new UserAddressDTO(1L, "Rua Teste", "São Paulo", "100", null, "SP", "00000-000", "Brasil");
        UserDTO exampleDto = new UserDTO(1L, "User", "teste@email.com", "login", "password", 1L, exampleAddressDTO);

        when(useCase.findByName("User")).thenReturn(List.of(exampleDto));

        mockMvc.perform(get("/v1/users/search/by-name").param("name", "User"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("User"));
    }

    @Test
    void findByEmail_shouldReturnDto() throws Exception {
        UserAddressDTO exampleAddressDTO = new UserAddressDTO(1L, "Rua Teste", "São Paulo", "100", null, "SP", "00000-000", "Brasil");
        UserDTO exampleDto = new UserDTO(1L, "User", "teste@email.com", "login", "password", 1L, exampleAddressDTO);

        when(useCase.findByEmail("teste@email.com")).thenReturn(exampleDto);

        mockMvc.perform(get("/v1/users/search/by-email").param("email", "teste@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("teste@email.com"));
    }

    @Test
    void findByEmail_whenNotFound_shouldReturnBadRequest() throws Exception {
        when(useCase.findByEmail("naoexiste@email.com")).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/v1/users/search/by-email").param("email", "naoexiste@email.com"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findByLogin_shouldReturnDto() throws Exception {
        UserAddressDTO exampleAddressDTO = new UserAddressDTO(1L, "Rua Teste", "São Paulo", "100", null, "SP", "00000-000", "Brasil");
        UserDTO exampleDto = new UserDTO(1L, "User", "teste@email.com", "login", "password", 1L, exampleAddressDTO);

        when(useCase.findByLogin("login")).thenReturn(exampleDto);

        mockMvc.perform(get("/v1/users/search/by-login").param("login", "login"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("login"));
    }

    @Test
    void findByLogin_whenNotFound_shouldReturnBadRequest() throws Exception {
        when(useCase.findByLogin("naoexiste")).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/v1/users/search/by-login").param("login", "naoexiste"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_shouldReturnUpdated() throws Exception {
        UserAddressDTO exampleAddressDTO = new UserAddressDTO(1L, "Rua Teste", "São Paulo", "100", null, "SP", "00000-000", "Brasil");
        UserDTO request = new UserDTO(null, "User Atualizado", "teste@email.com", "login", "password", 1L, exampleAddressDTO);
        UserDTO response = new UserDTO(1L, "User Atualizado", "teste@email.com", "login", "password", 1L, exampleAddressDTO);

        when(useCase.update(any(Long.class), any(UserDTO.class))).thenReturn(response);

        mockMvc.perform(put("/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("User Atualizado"));
    }

    @Test
    void update_whenNotFound_shouldReturnBadRequest() throws Exception {
        UserAddressDTO exampleAddressDTO = new UserAddressDTO(1L, "Rua Teste", "São Paulo", "100", null, "SP", "00000-000", "Brasil");
        UserDTO request = new UserDTO(null, "User Atualizado", "teste@email.com", "login", "password", 1L, exampleAddressDTO);

        when(useCase.update(any(Long.class), any(UserDTO.class))).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(put("/v1/users/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_shouldReturnNoContent() throws Exception {
        doNothing().when(useCase).delete(1L);

        mockMvc.perform(delete("/v1/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_whenNotFound_shouldReturnBadRequest() throws Exception {
        doThrow(new UserNotFoundException("User not found")).when(useCase).delete(99L);

        mockMvc.perform(delete("/v1/users/99"))
                .andExpect(status().isBadRequest());
    }
}
