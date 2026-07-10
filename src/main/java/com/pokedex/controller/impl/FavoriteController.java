package com.pokedex.controller.impl;

import com.pokedex.controller.api.FavoriteApi;
import com.pokedex.controller.dto.request.FavoriteRequest;
import com.pokedex.controller.dto.response.FavoriteResponse;
import com.pokedex.core.model.Favorite;
import com.pokedex.core.service.interfaces.FavoriteService;
import com.pokedex.security.CustomUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FavoriteController implements FavoriteApi {

    private final FavoriteService favoriteService;

    private Long uid(Authentication authentication) {
        return ((CustomUserDetails) authentication.getPrincipal()).getUserId();
    }

    private FavoriteResponse toResponse(Favorite f) {
        return new FavoriteResponse(f.getId(), f.getPokemonId(), f.getPokemonName(), f.getPokemonImageUrl(), f.getAddedAt());
    }

    @Override
    public ResponseEntity<List<FavoriteResponse>> findAll(Authentication authentication) {
        return ResponseEntity.ok(favoriteService.findAllByUser(uid(authentication)).stream()
                .map(this::toResponse).toList());
    }

    @Override
    public ResponseEntity<FavoriteResponse> add(Authentication authentication, FavoriteRequest request) {
        Favorite favorite = favoriteService.add(uid(authentication), request.pokemonId());
        return ResponseEntity.ok(toResponse(favorite));
    }

    @Override
    public ResponseEntity<Void> remove(Authentication authentication, Long pokemonId) {
        favoriteService.remove(uid(authentication), pokemonId);
        return ResponseEntity.noContent().build();
    }
}
