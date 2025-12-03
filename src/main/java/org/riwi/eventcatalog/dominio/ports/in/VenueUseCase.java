package org.riwi.eventcatalog.dominio.ports.in;

import org.riwi.eventcatalog.dominio.model.Venue;

import java.util.List;
import java.util.Optional;

public interface VenueUseCase {
    Venue createVenue(Venue venue);
    Optional<Venue> getVenueById(String id);
    List<Venue> getAllVenues();
    Venue updateVenue(String id, Venue venue);
    void deleteVenue(String id);
}
