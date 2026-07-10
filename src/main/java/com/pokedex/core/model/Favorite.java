package com.pokedex.core.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

/** Pokemon marcado como favorito por un usuario (RF-15). */
@Value
@Builder
public class Favorite {
    Long id;
    Long userId;
    Long pokemonId;
    String pokemonName;
    String pokemonImageUrl;
    LocalDateTime addedAt;
}
