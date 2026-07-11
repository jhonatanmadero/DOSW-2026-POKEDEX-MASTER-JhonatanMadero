package com.pokedex.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.core.model.Ability;
import com.pokedex.core.model.Pokemon;
import com.pokedex.core.model.PokemonFilterCriteria;
import com.pokedex.core.model.PokemonStats;
import com.pokedex.persistence.entity.relational.AbilityEntity;
import com.pokedex.persistence.entity.relational.PokemonEntity;
import com.pokedex.persistence.entity.relational.RegionEntity;
import com.pokedex.persistence.mapper.PokemonPersistenceMapper;
import com.pokedex.persistence.repository.relational.AbilityJpaRepository;
import com.pokedex.persistence.repository.relational.PokemonJpaRepository;
import com.pokedex.persistence.repository.relational.RegionJpaRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class PokemonPersistenceAdapterTest {

    @Mock private PokemonJpaRepository repository;
    @Mock private RegionJpaRepository regionRepository;
    @Mock private AbilityJpaRepository abilityRepository;
    @Mock private PokemonPersistenceMapper mapper;

    private PokemonPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new PokemonPersistenceAdapter(repository, regionRepository, abilityRepository, mapper);
    }

    private PokemonEntity pokemonEntity() {
        return PokemonEntity.builder().id(25L).nationalNumber(25).name("Pikachu").build();
    }

    private Pokemon pokemonDomain() {
        return Pokemon.builder().id(25L).nationalNumber(25).name("Pikachu").build();
    }

    @Test
    @DisplayName("RF-03: findAll mapea la pagina de entidades a dominio")
    void findAll_mapsPageToDomain() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<PokemonEntity> page = new PageImpl<>(List.of(pokemonEntity()));
        when(repository.findAllWithStatsAndRegion(pageable)).thenReturn(page);
        when(mapper.toDomain(pokemonEntity())).thenReturn(pokemonDomain());

        Page<Pokemon> result = adapter.findAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Pikachu");
    }

    @Test
    @DisplayName("RF-06: findById mapea la entidad con detalle")
    void findById_returnsMappedPokemon() {
        when(repository.findWithDetailsById(25L)).thenReturn(Optional.of(pokemonEntity()));
        when(mapper.toDomain(pokemonEntity())).thenReturn(pokemonDomain());

        assertThat(adapter.findById(25L)).contains(pokemonDomain());
    }

    @Test
    @DisplayName("RF-05: findByNationalNumber mapea la entidad con detalle")
    void findByNationalNumber_returnsMappedPokemon() {
        when(repository.findWithDetailsByNationalNumber(25)).thenReturn(Optional.of(pokemonEntity()));
        when(mapper.toDomain(pokemonEntity())).thenReturn(pokemonDomain());

        assertThat(adapter.findByNationalNumber(25)).contains(pokemonDomain());
    }

    @Test
    @DisplayName("RF-07..RF-14, RF-22: search delega en la specification dinamica")
    void search_delegatesToSpecification() {
        PokemonFilterCriteria criteria = PokemonFilterCriteria.builder().region("Kanto").build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<PokemonEntity> page = new PageImpl<>(List.of(pokemonEntity()));
        when(repository.findAll(any(Specification.class), org.mockito.ArgumentMatchers.eq(pageable))).thenReturn(page);
        when(mapper.toDomain(pokemonEntity())).thenReturn(pokemonDomain());

        List<Pokemon> result = adapter.search(criteria, pageable);

        assertThat(result).containsExactly(pokemonDomain());
    }

    @Test
    @DisplayName("existsByNationalNumber: delega al repositorio (RN-04)")
    void existsByNationalNumber_delegatesToRepository() {
        when(repository.existsByNationalNumber(25)).thenReturn(true);

        assertThat(adapter.existsByNationalNumber(25)).isTrue();
    }

    @Test
    @DisplayName("RF-20: save lanza 404 si la region no existe")
    void save_whenRegionMissing_throws() {
        Pokemon pokemon = Pokemon.builder().nationalNumber(999).name("Nuevo").region("Atlantis").build();
        when(regionRepository.findByNameIgnoreCase("Atlantis")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adapter.save(pokemon));
    }

    @Test
    @DisplayName("RF-20: save persiste el Pokemon con stats y habilidades")
    void save_persistsPokemonWithStatsAndAbilities() {
        Pokemon pokemon = Pokemon.builder()
                .nationalNumber(999).name("Nuevo").region("Kanto")
                .stats(PokemonStats.builder().hp(50).attack(50).defense(50)
                        .specialAttack(50).specialDefense(50).speed(50).build())
                .abilities(List.of(Ability.builder().name("Static").build()))
                .build();

        when(regionRepository.findByNameIgnoreCase("Kanto")).thenReturn(Optional.of(RegionEntity.builder().id(1L).name("Kanto").build()));
        when(abilityRepository.findByNameIgnoreCase("Static")).thenReturn(Optional.of(AbilityEntity.builder().id(1L).name("Static").build()));
        PokemonEntity saved = pokemonEntity();
        when(repository.save(any(PokemonEntity.class))).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(pokemonDomain());

        Pokemon result = adapter.save(pokemon);

        assertThat(result.getName()).isEqualTo("Pikachu");
    }

    @Test
    @DisplayName("RF-20: save crea la habilidad si no existe todavia")
    void save_createsAbilityWhenMissing() {
        Pokemon pokemon = Pokemon.builder()
                .nationalNumber(999).name("Nuevo").region("Kanto")
                .abilities(List.of(Ability.builder().name("Nueva Habilidad").build()))
                .build();

        when(regionRepository.findByNameIgnoreCase("Kanto")).thenReturn(Optional.of(RegionEntity.builder().id(1L).name("Kanto").build()));
        when(abilityRepository.findByNameIgnoreCase("Nueva Habilidad")).thenReturn(Optional.empty());
        when(abilityRepository.save(any(AbilityEntity.class)))
                .thenReturn(AbilityEntity.builder().id(2L).name("Nueva Habilidad").build());
        PokemonEntity saved = pokemonEntity();
        when(repository.save(any(PokemonEntity.class))).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(pokemonDomain());

        adapter.save(pokemon);

        org.mockito.Mockito.verify(abilityRepository).save(any(AbilityEntity.class));
    }

    @Test
    @DisplayName("deleteById: delega al repositorio")
    void deleteById_delegatesToRepository() {
        adapter.deleteById(25L);

        org.mockito.Mockito.verify(repository).deleteById(25L);
    }

    @Test
    @DisplayName("RF-12: findAllAbilityNames delega al repositorio")
    void findAllAbilityNames_delegatesToRepository() {
        when(repository.findAllAbilityNames()).thenReturn(List.of("Static", "Overgrow"));

        assertThat(adapter.findAllAbilityNames()).containsExactly("Static", "Overgrow");
    }
}