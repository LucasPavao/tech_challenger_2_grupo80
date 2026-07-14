package br.com.tech.challenger.api_restaurante.infrastructure.presentation.controller;

import br.com.tech.challenger.api_restaurante.application.dto.RestaurantDTO;
import br.com.tech.challenger.api_restaurante.application.dto.RestaurantRequestDTO;
import br.com.tech.challenger.api_restaurante.application.mapper.RestaurantDtoMapper;
import br.com.tech.challenger.api_restaurante.application.usecase.restaurant.*;
import br.com.tech.challenger.api_restaurante.infrastructure.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/restaurants")
@RequiredArgsConstructor
@Tag(name = "Restaurants", description = "Restaurant management endpoints")
public class RestaurantController {

    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final FindRestaurantUseCase findRestaurantUseCase;
    private final FindRestaurantByIdUseCase findRestaurantByIdUseCase;
    private final UpdateRestaurantUseCase updateRestaurantUseCase;
    private final DeleteRestaurantUseCase deleteRestaurantUseCase;

    private final RestaurantDtoMapper restaurantDtoMapper;

    @Operation(summary = "Create a new restaurant owned by the authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Restaurant created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestaurantDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
    })
    @PostMapping
    public ResponseEntity<RestaurantDTO> create(
            @RequestBody @Valid RestaurantRequestDTO dto,
            @AuthenticationPrincipal UserPrincipal principal) {
        RestaurantDTO restaurantDto = restaurantDtoMapper.toDto(
                createRestaurantUseCase.execute(dto, principal.getUser().getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantDto);
    }

    @Operation(summary = "Get all restaurants")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurants retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestaurantDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> findAll() {
        List<RestaurantDTO> restaurantDtoList = findRestaurantUseCase.execute().stream().map(restaurantDtoMapper::toDto).toList();
        return ResponseEntity.ok(restaurantDtoList);
    }

    @Operation(summary = "Get restaurant by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestaurantDTO.class))),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO> findById(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable Long id) {
        RestaurantDTO restaurant = restaurantDtoMapper.toDto(findRestaurantByIdUseCase.execute(id));
        return ResponseEntity.ok(restaurant);
    }

    @Operation(summary = "Update restaurant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestaurantDTO.class))),
            @ApiResponse(responseCode = "404", description = "Restaurant not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDTO> update(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable Long id,
            @RequestBody @Valid RestaurantRequestDTO dto) {
        RestaurantDTO updated = restaurantDtoMapper.toDto(updateRestaurantUseCase.execute(id, dto));
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete restaurant")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Restaurant deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable Long id) {
        deleteRestaurantUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

}
