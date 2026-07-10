package com.pokedex.persistence.adapter;

import com.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.core.model.Pokemon;
import com.pokedex.core.model.Team;
import com.pokedex.core.port.TeamPersistencePort;
import com.pokedex.persistence.entity.relational.PokemonEntity;
import com.pokedex.persistence.entity.relational.TeamEntity;
import com.pokedex.persistence.entity.relational.TeamPokemonEntity;
import com.pokedex.persistence.entity.relational.UserEntity;
import com.pokedex.persistence.mapper.TeamPersistenceMapper;
import com.pokedex.persistence.repository.relational.PokemonJpaRepository;
import com.pokedex.persistence.repository.relational.TeamJpaRepository;
import com.pokedex.persistence.repository.relational.UserJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamPersistenceAdapter implements TeamPersistencePort {

    private final TeamJpaRepository repository;
    private final UserJpaRepository userRepository;
    private final PokemonJpaRepository pokemonRepository;
    private final TeamPersistenceMapper mapper;

    @Override
    public Optional<Team> findById(Long id) {
        return repository.findWithDetailsById(id).map(mapper::toDomain);
    }

    @Override
    public List<Team> findAllByUserId(Long userId) {
        return repository.findByUser_Id(userId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Team save(Team team) {
        UserEntity user = userRepository.findById(team.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", team.getUserId()));

        TeamEntity entity = TeamEntity.builder()
                .id(team.getId())
                .user(user)
                .name(team.getName())
                .description(team.getDescription())
                .build();

        List<TeamPokemonEntity> members = new java.util.ArrayList<>();
        List<Pokemon> pokemons = team.getPokemons() != null ? team.getPokemons() : List.of();
        for (int i = 0; i < pokemons.size(); i++) {
            Long pokemonId = pokemons.get(i).getId();
            PokemonEntity pokemonEntity = pokemonRepository.findById(pokemonId)
                    .orElseThrow(() -> new ResourceNotFoundException("Pokemon", "id", pokemonId));
            members.add(TeamPokemonEntity.builder()
                    .team(entity)
                    .pokemon(pokemonEntity)
                    .slotOrder(i + 1)
                    .build());
        }
        entity.replaceMembers(members);

        TeamEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public long countTeamsContainingPokemon(Long pokemonId) {
        return repository.countTeamsContainingPokemon(pokemonId);
    }

    @Override
    public long countAll() {
        return repository.count();
    }
}
