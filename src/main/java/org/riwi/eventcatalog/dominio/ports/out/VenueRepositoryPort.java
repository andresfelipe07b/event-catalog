package org.riwi.eventcatalog.dominio.ports.out;

import org.riwi.eventcatalog.dominio.model.Venue;

import java.util.List;
import java.util.Optional;

public interface VenueRepositoryPort {
    Venue save(Venue venue);
    Optional<Venue> findById(String id);
    List<Venue> findAll();
    void deleteById(String id);
}
