package org.riwi.eventcatalog.aplicacion.usecase;

import lombok.AllArgsConstructor;
import org.riwi.eventcatalog.dominio.exception.IdNotFoundException;
import org.riwi.eventcatalog.dominio.model.Venue;
import org.riwi.eventcatalog.dominio.ports.in.VenueUseCase;
import org.riwi.eventcatalog.dominio.ports.out.VenueRepositoryPort;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class VenueService implements VenueUseCase {

    private final VenueRepositoryPort venueRepositoryPort;

    @Override
    public Venue createVenue(Venue venue) {
        return venueRepositoryPort.save(venue);
    }

    @Override
    public Optional<Venue> getVenueById(String id) {
        return venueRepositoryPort.findById(id);
    }

    @Override
    public List<Venue> getAllVenues() {
        return venueRepositoryPort.findAll();
    }

    @Override
    public Venue updateVenue(String id, Venue venue) {
        return venueRepositoryPort.findById(id).map(existingVenue -> {
            venue.setId(id);
            return venueRepositoryPort.save(venue);
        }).orElseThrow(() -> new IdNotFoundException("Venue")); // Usar excepción de dominio
    }

    @Override
    public void deleteVenue(String id) {
        venueRepositoryPort.deleteById(id);
    }
}
