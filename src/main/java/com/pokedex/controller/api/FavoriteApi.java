package com.pokedex.controller.api;

import com.pokedex.controller.dto.request.FavoriteRequest;
import com.pokedex.controller.dto.response.FavoriteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/** RF-15: favoritos. Requiere usuario autenticado (RN-05). */
@Tag(name = "Favorites", description = "Pokemon favoritos del usuario")
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/v1/favorites")
public interface FavoriteApi {

    @Operation(summary = "Listar mis favoritos")
    @GetMapping
    ResponseEntity<List<FavoriteResponse>> findAll(Authentication authentication);

    @Operation(summary = "Agregar favorito")
    @PostMapping
    ResponseEntity<FavoriteResponse> add(Authentication authentication, @Valid @RequestBody FavoriteRequest request);

    @Operation(summary = "Quitar favorito")
    @DeleteMapping("/{pokemonId}")
    ResponseEntity<Void> remove(Authentication authentication, @PathVariable Long pokemonId);
}
