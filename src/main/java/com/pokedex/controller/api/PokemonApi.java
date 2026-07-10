package com.pokedex.controller.api;

import com.pokedex.controller.dto.request.PokemonRequest;
import com.pokedex.controller.dto.response.PokemonResponse;
import com.pokedex.controller.dto.response.PokemonSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** RF-03 a RF-14, RF-20, RF-22: catalogo de Pokemon, busqueda, filtros y CRUD de administrador. */
@Tag(name = "Pokemon", description = "Consulta y gestion del catalogo de Pokemon")
@RequestMapping("/v1/pokemon")
public interface PokemonApi {

    @Operation(summary = "Listar Pokemon", description = "RF-03: listado paginado. Acceso publico.")
    @GetMapping
    ResponseEntity<Page<PokemonSummaryResponse>> findAll(
            @PageableDefault(size = 20, sort = "nationalNumber") Pageable pageable);

    @Operation(summary = "Buscar por nombre", description = "RF-04: coincidencia parcial, minimo 2 caracteres.")
    @GetMapping("/search")
    ResponseEntity<List<PokemonSummaryResponse>> searchByName(
            @Parameter(description = "Termino de busqueda") @RequestParam("q") String term,
            @PageableDefault(size = 20) Pageable pageable);

    @Operation(summary = "Buscar por numero de Pokedex", description = "RF-05")
    @GetMapping("/number/{number}")
    ResponseEntity<PokemonResponse> findByNationalNumber(@PathVariable Integer number);

    @Operation(summary = "Filtrar catalogo", description = "RF-07 a RF-14, RF-22: filtros combinables.")
    @GetMapping("/filter")
    ResponseEntity<List<PokemonSummaryResponse>> filter(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String typePrimary,
            @RequestParam(required = false) String typeSecondary,
            @RequestParam(required = false) Integer generation,
            @RequestParam(required = false) String statName,
            @RequestParam(required = false) Integer statMin,
            @RequestParam(required = false) Integer statMax,
            @RequestParam(required = false) String ability,
            @RequestParam(required = false) Boolean hasMega,
            @RequestParam(required = false) String evolutionStage,
            @RequestParam(required = false) Double weightMin,
            @RequestParam(required = false) Double weightMax,
            @PageableDefault(size = 20) Pageable pageable);

    @Operation(summary = "Listar habilidades disponibles", description = "Soporte para RF-12.")
    @GetMapping("/abilities")
    ResponseEntity<List<String>> listAbilities();

    @Operation(summary = "Obtener detalle de un Pokemon", description = "RF-06")
    @GetMapping("/{id}")
    ResponseEntity<PokemonResponse> findById(@PathVariable Long id);

    @Operation(summary = "Crear Pokemon", description = "RF-20: solo ADMIN")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    ResponseEntity<PokemonResponse> create(@Valid @RequestBody PokemonRequest request);

    @Operation(summary = "Actualizar Pokemon", description = "RF-20: solo ADMIN")
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{id}")
    ResponseEntity<PokemonResponse> update(@PathVariable Long id, @Valid @RequestBody PokemonRequest request);

    @Operation(summary = "Eliminar Pokemon", description = "RF-20: solo ADMIN")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}
