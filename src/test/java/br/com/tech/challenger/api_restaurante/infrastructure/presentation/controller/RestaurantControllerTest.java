package br.com.tech.challenger.api_restaurante.infrastructure.presentation.controller;

import br.com.tech.challenger.api_restaurante.application.dto.RestaurantRequestDTO;
import br.com.tech.challenger.api_restaurante.application.exception.RestaurantNotFoundException;
import br.com.tech.challenger.api_restaurante.application.exception.UserNotAnOwnerException;
import br.com.tech.challenger.api_restaurante.application.exception.UserNotFoundException;
import br.com.tech.challenger.api_restaurante.application.mapper.RestaurantDtoMapper;
import br.com.tech.challenger.api_restaurante.application.mapper.UserAddressDtoMapper;
import br.com.tech.challenger.api_restaurante.application.mapper.UserDtoMapper;
import br.com.tech.challenger.api_restaurante.application.usecase.restaurant.CreateRestaurantUseCase;
import br.com.tech.challenger.api_restaurante.application.usecase.restaurant.DeleteRestaurantUseCase;
import br.com.tech.challenger.api_restaurante.application.usecase.restaurant.FindRestaurantByIdUseCase;
import br.com.tech.challenger.api_restaurante.application.usecase.restaurant.FindRestaurantUseCase;
import br.com.tech.challenger.api_restaurante.application.usecase.restaurant.UpdateRestaurantUseCase;
import br.com.tech.challenger.api_restaurante.domain.entity.Restaurant;
import br.com.tech.challenger.api_restaurante.domain.entity.User;
import br.com.tech.challenger.api_restaurante.domain.entity.UserType;
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

class RestaurantControllerTest {

    private MockMvc mockMvc;

    private CreateRestaurantUseCase createRestaurantUseCase;
    private FindRestaurantUseCase findRestaurantUseCase;
    private FindRestaurantByIdUseCase findRestaurantByIdUseCase;
    private UpdateRestaurantUseCase updateRestaurantUseCase;
    private DeleteRestaurantUseCase deleteRestaurantUseCase;

    private ObjectMapper objectMapper;

    private Restaurant exampleRestaurant;
    private RestaurantRequestDTO exampleRequest;

    @BeforeEach
    void setUp() {
        createRestaurantUseCase = Mockito.mock(CreateRestaurantUseCase.class);
        findRestaurantUseCase = Mockito.mock(FindRestaurantUseCase.class);
        findRestaurantByIdUseCase = Mockito.mock(FindRestaurantByIdUseCase.class);
        updateRestaurantUseCase = Mockito.mock(UpdateRestaurantUseCase.class);
        deleteRestaurantUseCase = Mockito.mock(DeleteRestaurantUseCase.class);

        RestaurantDtoMapper restaurantDtoMapper = new RestaurantDtoMapper(new UserDtoMapper(new UserAddressDtoMapper()));

        RestaurantController controller = new RestaurantController(
                createRestaurantUseCase,
                findRestaurantUseCase,
                findRestaurantByIdUseCase,
                updateRestaurantUseCase,
                deleteRestaurantUseCase,
                restaurantDtoMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();

        UserType ownerType = UserType.builder().id(1L).name("RESTAURANT_OWNER").build();
        User owner = User.builder().id(1L).name("Owner").email("owner@email.com").login("owner").userType(ownerType).build();

        exampleRestaurant = Restaurant.builder()
                .id(1L)
                .name("Restaurant")
                .address("Address")
                .cuisineType("Brazilian")
                .operatingHours("10:00-22:00")
                .owner(owner)
                .build();

        exampleRequest = new RestaurantRequestDTO("Restaurant", "Address", "Brazilian", "10:00-22:00", 1L);
    }

    @Test
    void create_shouldReturnCreated() throws Exception {
        when(createRestaurantUseCase.execute(any(RestaurantRequestDTO.class))).thenReturn(exampleRestaurant);

        mockMvc.perform(post("/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exampleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Restaurant"));
    }

    @Test
    void create_whenOwnerNotFound_shouldReturnBadRequest() throws Exception {
        when(createRestaurantUseCase.execute(any(RestaurantRequestDTO.class)))
                .thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(post("/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exampleRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_whenUserNotAnOwner_shouldReturnBadRequest() throws Exception {
        when(createRestaurantUseCase.execute(any(RestaurantRequestDTO.class)))
                .thenThrow(new UserNotAnOwnerException("User user is not a restaurant owner."));

        mockMvc.perform(post("/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exampleRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAll_shouldReturnList() throws Exception {
        when(findRestaurantUseCase.execute()).thenReturn(List.of(exampleRestaurant));

        mockMvc.perform(get("/v1/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Restaurant"));
    }

    @Test
    void findById_shouldReturnDto() throws Exception {
        when(findRestaurantByIdUseCase.execute(1L)).thenReturn(exampleRestaurant);

        mockMvc.perform(get("/v1/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Restaurant"));
    }

    @Test
    void findById_whenNotFound_shouldReturnNotFound() throws Exception {
        when(findRestaurantByIdUseCase.execute(99L)).thenThrow(new RestaurantNotFoundException("Restaurant not found"));

        mockMvc.perform(get("/v1/restaurants/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_shouldReturnUpdated() throws Exception {
        when(updateRestaurantUseCase.execute(any(Long.class), any(RestaurantRequestDTO.class))).thenReturn(exampleRestaurant);

        mockMvc.perform(put("/v1/restaurants/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exampleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void update_whenNotFound_shouldReturnNotFound() throws Exception {
        when(updateRestaurantUseCase.execute(any(Long.class), any(RestaurantRequestDTO.class)))
                .thenThrow(new RestaurantNotFoundException("Restaurant not found"));

        mockMvc.perform(put("/v1/restaurants/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exampleRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldReturnNoContent() throws Exception {
        doNothing().when(deleteRestaurantUseCase).execute(1L);

        mockMvc.perform(delete("/v1/restaurants/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_whenNotFound_shouldReturnNotFound() throws Exception {
        doThrow(new RestaurantNotFoundException("Restaurant not found")).when(deleteRestaurantUseCase).execute(99L);

        mockMvc.perform(delete("/v1/restaurants/99"))
                .andExpect(status().isNotFound());
    }
}
