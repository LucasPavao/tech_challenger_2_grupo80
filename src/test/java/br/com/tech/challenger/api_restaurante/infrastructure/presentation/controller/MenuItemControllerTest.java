package br.com.tech.challenger.api_restaurante.infrastructure.presentation.controller;

import br.com.tech.challenger.api_restaurante.application.dto.MenuItemDTO;
import br.com.tech.challenger.api_restaurante.application.dto.MenuItemRequestDTO;
import br.com.tech.challenger.api_restaurante.application.dto.RestaurantDTO;
import br.com.tech.challenger.api_restaurante.application.dto.UserAddressDTO;
import br.com.tech.challenger.api_restaurante.application.dto.UserDTO;
import br.com.tech.challenger.api_restaurante.application.exception.MenuItemNotFoundException;
import br.com.tech.challenger.api_restaurante.application.exception.RestaurantNotFoundException;
import br.com.tech.challenger.api_restaurante.application.usecase.menuitem.CreateMenuItemUseCase;
import br.com.tech.challenger.api_restaurante.application.usecase.menuitem.DeleteMenuItemUseCase;
import br.com.tech.challenger.api_restaurante.application.usecase.menuitem.FindMenuItemByIdUseCase;
import br.com.tech.challenger.api_restaurante.application.usecase.menuitem.FindMenuItemUseCase;
import br.com.tech.challenger.api_restaurante.application.usecase.menuitem.UpdateMenuItemUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MenuItemControllerTest {

    private MockMvc mockMvc;

    private CreateMenuItemUseCase createMenuItemUseCase;
    private FindMenuItemUseCase findMenuItemUseCase;
    private FindMenuItemByIdUseCase findMenuItemByIdUseCase;
    private UpdateMenuItemUseCase updateMenuItemUseCase;
    private DeleteMenuItemUseCase deleteMenuItemUseCase;

    private ObjectMapper objectMapper;

    private RestaurantDTO exampleRestaurantDTO;

    @BeforeEach
    void setUp() {
        createMenuItemUseCase = Mockito.mock(CreateMenuItemUseCase.class);
        findMenuItemUseCase = Mockito.mock(FindMenuItemUseCase.class);
        findMenuItemByIdUseCase = Mockito.mock(FindMenuItemByIdUseCase.class);
        updateMenuItemUseCase = Mockito.mock(UpdateMenuItemUseCase.class);
        deleteMenuItemUseCase = Mockito.mock(DeleteMenuItemUseCase.class);

        MenuItemController controller = new MenuItemController(
                createMenuItemUseCase,
                findMenuItemUseCase,
                findMenuItemByIdUseCase,
                updateMenuItemUseCase,
                deleteMenuItemUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();

        UserAddressDTO addressDTO = new UserAddressDTO(1L, "Rua Teste", "São Paulo", "100", null, "SP", "00000-000", "Brasil");
        UserDTO ownerDTO = new UserDTO(1L, "Owner", "owner@email.com", "owner", null, 1L, addressDTO);
        exampleRestaurantDTO = new RestaurantDTO(1L, "Restaurant", "Address", "Brazilian", "10:00-22:00", ownerDTO);
    }

    private MenuItemDTO exampleMenuItemDTO() {
        return new MenuItemDTO(1L, "Rice and Beans", "Rice and Beans", BigDecimal.valueOf(15.20), true, null, exampleRestaurantDTO, null, null);
    }

    private MenuItemRequestDTO exampleRequestDTO() {
        return new MenuItemRequestDTO("Rice and Beans", "Rice and Beans", BigDecimal.valueOf(15.20), true, null, 1L);
    }

    @Test
    void create_shouldReturnCreated() throws Exception {
        when(createMenuItemUseCase.execute(any(MenuItemRequestDTO.class))).thenReturn(exampleMenuItemDTO());

        mockMvc.perform(post("/v1/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exampleRequestDTO())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rice and Beans"));
    }

    @Test
    void create_whenRestaurantNotFound_shouldReturnNotFound() throws Exception {
        when(createMenuItemUseCase.execute(any(MenuItemRequestDTO.class)))
                .thenThrow(new RestaurantNotFoundException("Restaurant not found with id: 1"));

        mockMvc.perform(post("/v1/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exampleRequestDTO())))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAll_shouldReturnList() throws Exception {
        when(findMenuItemUseCase.execute()).thenReturn(List.of(exampleMenuItemDTO()));

        mockMvc.perform(get("/v1/menu-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Rice and Beans"));
    }

    @Test
    void findAll_byRestaurantId_shouldReturnFilteredList() throws Exception {
        when(findMenuItemUseCase.executeByRestaurantId(1L)).thenReturn(List.of(exampleMenuItemDTO()));

        mockMvc.perform(get("/v1/menu-items").param("restaurantId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void findById_shouldReturnDto() throws Exception {
        when(findMenuItemByIdUseCase.execute(1L)).thenReturn(exampleMenuItemDTO());

        mockMvc.perform(get("/v1/menu-items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rice and Beans"));
    }

    @Test
    void findById_whenNotFound_shouldReturnNotFound() throws Exception {
        when(findMenuItemByIdUseCase.execute(99L)).thenThrow(new MenuItemNotFoundException("Menu item not found with id: 99"));

        mockMvc.perform(get("/v1/menu-items/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_shouldReturnUpdated() throws Exception {
        when(updateMenuItemUseCase.execute(any(Long.class), any(MenuItemRequestDTO.class))).thenReturn(exampleMenuItemDTO());

        mockMvc.perform(put("/v1/menu-items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exampleRequestDTO())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void update_whenNotFound_shouldReturnNotFound() throws Exception {
        when(updateMenuItemUseCase.execute(any(Long.class), any(MenuItemRequestDTO.class)))
                .thenThrow(new MenuItemNotFoundException("Menu item not found with id: 99"));

        mockMvc.perform(put("/v1/menu-items/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exampleRequestDTO())))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldReturnNoContent() throws Exception {
        doNothing().when(deleteMenuItemUseCase).execute(1L);

        mockMvc.perform(delete("/v1/menu-items/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_whenNotFound_shouldReturnNotFound() throws Exception {
        doThrow(new MenuItemNotFoundException("Menu item not found with id: 99")).when(deleteMenuItemUseCase).execute(99L);

        mockMvc.perform(delete("/v1/menu-items/99"))
                .andExpect(status().isNotFound());
    }
}
