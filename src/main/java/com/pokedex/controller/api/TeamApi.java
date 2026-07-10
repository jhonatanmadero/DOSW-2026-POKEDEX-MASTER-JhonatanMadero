package com.pokedex.controller.api;

import com.pokedex.controller.dto.request.TeamRequest;
import com.pokedex.controller.dto.response.TeamAnalysisResponse;
import com.pokedex.controller.dto.response.TeamResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/** RF-16, RF-17, RF-18: equipos Pokemon. */
@Tag(name = "Teams", description = "Equipos Pokemon del usuario")
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/v1/teams")
public interface TeamApi {

    @Operation(summary = "Crear equipo", description = "RF-16")
    @PostMapping
    ResponseEntity<TeamResponse> create(Authentication authentication, @Valid @RequestBody TeamRequest request);

    @Operation(summary = "Listar mis equipos", description = "RF-18")
    @GetMapping
    ResponseEntity<List<TeamResponse>> findAll(Authentication authentication);

    @Operation(summary = "Obtener un equipo", description = "RF-18")
    @GetMapping("/{id}")
    ResponseEntity<TeamResponse> findById(Authentication authentication, @PathVariable Long id);

    @Operation(summary = "Actualizar equipo", description = "RF-18")
    @PutMapping("/{id}")
    ResponseEntity<TeamResponse> update(Authentication authentication, @PathVariable Long id,
                                         @Valid @RequestBody TeamRequest request);

    @Operation(summary = "Eliminar equipo", description = "RF-18")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(Authentication authentication, @PathVariable Long id);

    @Operation(summary = "Analisis competitivo del equipo", description = "RF-17")
    @GetMapping("/{id}/analysis")
    ResponseEntity<TeamAnalysisResponse> analyze(Authentication authentication, @PathVariable Long id);
}
