package org.riwi.eventcatalog.infraestructura.adapters.out.jpa;

import lombok.AllArgsConstructor;
import org.riwi.eventcatalog.dominio.model.Venue;
import org.riwi.eventcatalog.dominio.ports.out.VenueRepositoryPort;
import org.riwi.eventcatalog.infraestructura.adapters.out.jpa.entity.VenueJpaEntity;
import org.riwi.eventcatalog.infraestructura.adapters.out.jpa.mapper.VenueJpaMapper;
import org.riwi.eventcatalog.infraestructura.adapters.out.jpa.repository.VenueJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class VenueRepositoryAdapter implements VenueRepositoryPort {

    private final VenueJpaRepository venueJpaRepository;
    private final VenueJpaMapper venueJpaMapper;

    @Override
    public Venue save(Venue venue) {
        VenueJpaEntity venueJpaEntity = venueJpaMapper.toEntity(venue);
        VenueJpaEntity savedEntity = venueJpaRepository.save(venueJpaEntity);
        return venueJpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Venue> findById(String id) {
        return venueJpaRepository.findById(id).map(venueJpaMapper::toDomain);
    }

    @Override
    public List<Venue> findAll() {
        return venueJpaRepository.findAll().stream()
                .map(venueJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        venueJpaRepository.deleteById(id);
    }
}
