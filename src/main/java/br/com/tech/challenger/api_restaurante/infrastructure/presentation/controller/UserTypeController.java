package br.com.tech.challenger.api_restaurante.infrastructure.presentation.controller;

import br.com.tech.challenger.api_restaurante.application.dto.UserTypeDTO;
import br.com.tech.challenger.api_restaurante.application.usecase.usertype.UserTypeUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/user-types")
@RequiredArgsConstructor
@Tag(name = "User Types", description = "User type management endpoints")
public class UserTypeController {

    private final UserTypeUseCase userTypeUseCase;

    @Operation(summary = "Create a new user type")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User type created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserTypeDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "User type already exists")
    })
    @PostMapping
    public ResponseEntity<UserTypeDTO> create(@RequestBody UserTypeDTO dto) {
        UserTypeDTO created = userTypeUseCase.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Get all user types")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User types retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserTypeDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<UserTypeDTO>> findAll() {
        List<UserTypeDTO> userTypes = userTypeUseCase.findAll();
        return ResponseEntity.ok(userTypes);
    }

    @Operation(summary = "Get user type by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User type retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserTypeDTO.class))),
            @ApiResponse(responseCode = "404", description = "User type not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserTypeDTO> findById(
            @Parameter(description = "User type ID", required = true)
            @PathVariable Long id) {
        UserTypeDTO userType = userTypeUseCase.findById(id);
        return ResponseEntity.ok(userType);
    }

    @Operation(summary = "Update user type")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User type updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserTypeDTO.class))),
            @ApiResponse(responseCode = "404", description = "User type not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "User type already exists")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserTypeDTO> update(
            @Parameter(description = "User type ID", required = true)
            @PathVariable Long id,
            @RequestBody UserTypeDTO dto) {
        UserTypeDTO updated = userTypeUseCase.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete user type")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User type deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User type not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "User type ID", required = true)
            @PathVariable Long id) {
        userTypeUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}

