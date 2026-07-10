package com.pokedex.persistence.adapter;

import com.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.core.model.Ability;
import com.pokedex.core.model.Pokemon;
import com.pokedex.core.model.PokemonFilterCriteria;
import com.pokedex.core.model.PokemonStats;
import com.pokedex.core.port.PokemonPersistencePort;
import com.pokedex.persistence.entity.relational.AbilityEntity;
import com.pokedex.persistence.entity.relational.PokemonAbilityEntity;
import com.pokedex.persistence.entity.relational.PokemonEntity;
import com.pokedex.persistence.entity.relational.PokemonStatsEntity;
import com.pokedex.persistence.entity.relational.RegionEntity;
import com.pokedex.persistence.mapper.PokemonPersistenceMapper;
import com.pokedex.persistence.repository.relational.AbilityJpaRepository;
import com.pokedex.persistence.repository.relational.PokemonJpaRepository;
import com.pokedex.persistence.repository.relational.RegionJpaRepository;
import com.pokedex.persistence.specification.PokemonSpecification;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PokemonPersistenceAdapter implements PokemonPersistencePort {

    private final PokemonJpaRepository repository;
    private final RegionJpaRepository regionRepository;
    private final AbilityJpaRepository abilityRepository;
    private final PokemonPersistenceMapper mapper;

    @Override
    public Page<Pokemon> findAll(Pageable pageable) {
        return repository.findAllWithStatsAndRegion(pageable).map(mapper::toDomain);
    }

    @Override
    public Optional<Pokemon> findById(Long id) {
        return repository.findWithDetailsById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Pokemon> findByNationalNumber(Integer number) {
        return repository.findWithDetailsByNationalNumber(number).map(mapper::toDomain);
    }

    @Override
    public List<Pokemon> search(PokemonFilterCriteria criteria, Pageable pageable) {
        return repository.findAll(PokemonSpecification.fromCriteria(criteria), pageable)
                .map(mapper::toDomain)
                .getContent();
    }

    @Override
    public boolean existsByNationalNumber(Integer number) {
        return repository.existsByNationalNumber(number);
    }

    @Override
    public Pokemon save(Pokemon pokemon) {
        RegionEntity region = regionRepository.findByNameIgnoreCase(pokemon.getRegion())
                .orElseThrow(() -> new ResourceNotFoundException("Region", "name", pokemon.getRegion()));

        PokemonEntity.PokemonEntityBuilder builder = PokemonEntity.builder()
                .id(pokemon.getId())
                .nationalNumber(pokemon.getNationalNumber())
                .name(pokemon.getName())
                .description(pokemon.getDescription())
                .imageUrl(pokemon.getImageUrl())
                .typePrimary(pokemon.getTypePrimary())
                .typeSecondary(pokemon.getTypeSecondary())
                .region(region)
                .generation(pokemon.getGeneration())
                .heightMeters(pokemon.getHeightMeters())
                .weightKg(pokemon.getWeightKg())
                .hasMega(pokemon.getHasMega() != null ? pokemon.getHasMega() : false)
                .evolutionStage(pokemon.getEvolutionStage());

        PokemonEntity entity = builder.build();

        if (pokemon.getStats() != null) {
            PokemonStats s = pokemon.getStats();
            PokemonStatsEntity statsEntity = PokemonStatsEntity.builder()
                    .hp(s.getHp()).attack(s.getAttack()).defense(s.getDefense())
                    .specialAttack(s.getSpecialAttack()).specialDefense(s.getSpecialDefense())
                    .speed(s.getSpeed())
                    .build();
            entity.assignStats(statsEntity);
        }

        if (pokemon.getAbilities() != null && !pokemon.getAbilities().isEmpty()) {
            List<PokemonAbilityEntity> abilityLinks = pokemon.getAbilities().stream()
                    .map(this::resolveOrCreateAbility)
                    .map(a -> PokemonAbilityEntity.builder().pokemon(entity).ability(a).build())
                    .toList();
            entity.replaceAbilities(abilityLinks);
        }

        PokemonEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    private AbilityEntity resolveOrCreateAbility(Ability ability) {
        return abilityRepository.findByNameIgnoreCase(ability.getName())
                .orElseGet(() -> abilityRepository.save(AbilityEntity.builder()
                        .name(ability.getName())
                        .description(ability.getDescription())
                        .isHidden(ability.getIsHidden() != null ? ability.getIsHidden() : false)
                        .build()));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<String> findAllAbilityNames() {
        return repository.findAllAbilityNames();
    }
}
