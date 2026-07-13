package br.com.tech.challenger.api_restaurante.infrastructure.presentation.controller;

import br.com.tech.challenger.api_restaurante.application.dto.AuthenticatedUserResponseDTO;
import br.com.tech.challenger.api_restaurante.application.dto.LoginRequestDTO;
import br.com.tech.challenger.api_restaurante.application.dto.RefreshTokenRequestDTO;
import br.com.tech.challenger.api_restaurante.application.dto.RegisterRequestDTO;
import br.com.tech.challenger.api_restaurante.application.dto.TokenResponseDTO;
import br.com.tech.challenger.api_restaurante.application.dto.UserAddressDTO;
import br.com.tech.challenger.api_restaurante.application.dto.UserDTO;
import br.com.tech.challenger.api_restaurante.application.exception.EmailAlreadyExistsException;
import br.com.tech.challenger.api_restaurante.application.exception.InvalidTokenException;
import br.com.tech.challenger.api_restaurante.application.usecase.auth.LoginUseCase;
import br.com.tech.challenger.api_restaurante.application.usecase.auth.RefreshTokenUseCase;
import br.com.tech.challenger.api_restaurante.application.usecase.auth.RegisterUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {

    private MockMvc mockMvc;

    private LoginUseCase loginUseCase;
    private RefreshTokenUseCase refreshTokenUseCase;
    private RegisterUseCase registerUseCase;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        loginUseCase = Mockito.mock(LoginUseCase.class);
        refreshTokenUseCase = Mockito.mock(RefreshTokenUseCase.class);
        registerUseCase = Mockito.mock(RegisterUseCase.class);

        AuthController controller = new AuthController(loginUseCase, refreshTokenUseCase, registerUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void login_shouldReturnOk() throws Exception {
        UserAddressDTO addressDTO = new UserAddressDTO(1L, "Rua Teste", "São Paulo", "100", null, "SP", "00000-000", "Brasil");
        UserDTO userDTO = new UserDTO(1L, "John Smith", "john@email.com", "john.smith", null, 1L, addressDTO);
        TokenResponseDTO tokenDTO = new TokenResponseDTO("access-token", "refresh-token", Instant.now().plusSeconds(3600));

        when(loginUseCase.execute(any(LoginRequestDTO.class))).thenReturn(new AuthenticatedUserResponseDTO(userDTO, tokenDTO));

        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequestDTO("john.smith", "password"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.login").value("john.smith"))
                .andExpect(jsonPath("$.token.accessToken").value("access-token"));
    }

    @Test
    void refreshToken_shouldReturnOk() throws Exception {
        TokenResponseDTO tokenDTO = new TokenResponseDTO("new-access-token", "new-refresh-token", Instant.now().plusSeconds(3600));

        when(refreshTokenUseCase.execute(any(RefreshTokenRequestDTO.class))).thenReturn(tokenDTO);

        mockMvc.perform(post("/v1/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RefreshTokenRequestDTO("refresh-token"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-access-token"));
    }

    @Test
    void refreshToken_whenInvalid_shouldReturnUnauthorized() throws Exception {
        when(refreshTokenUseCase.execute(any(RefreshTokenRequestDTO.class)))
                .thenThrow(new InvalidTokenException("Invalid or expired token"));

        mockMvc.perform(post("/v1/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RefreshTokenRequestDTO("bad-token"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void register_shouldReturnCreatedWithTokens() throws Exception {
        UserAddressDTO addressDTO = new UserAddressDTO(1L, "Rua Teste", "São Paulo", "100", null, "SP", "00000-000", "Brasil");
        UserDTO userDTO = new UserDTO(1L, "John Smith", "john@email.com", "john.smith", null, 3L, addressDTO);
        TokenResponseDTO tokenDTO = new TokenResponseDTO("access-token", "refresh-token", Instant.now().plusSeconds(3600));

        when(registerUseCase.execute(any(RegisterRequestDTO.class))).thenReturn(new AuthenticatedUserResponseDTO(userDTO, tokenDTO));

        RegisterRequestDTO request = new RegisterRequestDTO("John Smith", "john@email.com", "john.smith", "StrongPass@123", addressDTO, null);

        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.login").value("john.smith"))
                .andExpect(jsonPath("$.token.accessToken").value("access-token"));
    }

    @Test
    void register_whenEmailAlreadyExists_shouldReturnConflict() throws Exception {
        UserAddressDTO addressDTO = new UserAddressDTO(1L, "Rua Teste", "São Paulo", "100", null, "SP", "00000-000", "Brasil");
        RegisterRequestDTO request = new RegisterRequestDTO("John Smith", "john@email.com", "john.smith", "StrongPass@123", addressDTO, null);

        when(registerUseCase.execute(any(RegisterRequestDTO.class)))
                .thenThrow(new EmailAlreadyExistsException("Email already exists"));

        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}
