package org.riwi.eventcatalog.aplicacion.usecase;

import lombok.AllArgsConstructor;
import org.riwi.eventcatalog.dominio.exception.IdNotFoundException;
import org.riwi.eventcatalog.dominio.model.Event;
import org.riwi.eventcatalog.dominio.model.EventSearchCriteria;
import org.riwi.eventcatalog.dominio.model.Venue;
import org.riwi.eventcatalog.dominio.ports.in.EventUseCase;
import org.riwi.eventcatalog.dominio.ports.out.EventRepositoryPort;
import org.riwi.eventcatalog.dominio.ports.out.VenueRepositoryPort;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class EventService implements EventUseCase {

    private final EventRepositoryPort eventRepositoryPort;
    private final VenueRepositoryPort venueRepositoryPort;

    @Override
    public Event createEvent(Event event) {
        eventRepositoryPort.findByName(event.getName()).ifPresent(e -> {
            throw new IllegalArgumentException("El nombre del evento ya existe");
        });

        Venue venue = venueRepositoryPort.findById(event.getVenue().getId())
                .orElseThrow(() -> new IdNotFoundException("Venue"));
        event.setVenue(venue); // Asegurar que el venue del evento es el del dominio

        return eventRepositoryPort.save(event);
    }

    @Override
    public Optional<Event> getEventById(String id) {
        return eventRepositoryPort.findById(id);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepositoryPort.findAll();
    }

    @Override
    public List<Event> getAllEventsByCriteria(EventSearchCriteria criteria) {
        return eventRepositoryPort.findAllByCriteria(criteria);
    }

    @Override
    public Event updateEvent(String id, Event event) {
        Event existingEvent = eventRepositoryPort.findById(id)
                .orElseThrow(() -> new IdNotFoundException("Event"));

        eventRepositoryPort.findByName(event.getName()).ifPresent(e -> {
            if (!e.getId().equals(id)) {
                throw new IllegalArgumentException("El nombre del evento ya existe");
            }
        });

        Venue venue = venueRepositoryPort.findById(event.getVenue().getId())
                .orElseThrow(() -> new IdNotFoundException("Venue"));

        event.setId(id); // Asegurar que el ID del evento a actualizar es el correcto
        event.setVenue(venue); // Asegurar que el venue del evento es el del dominio

        return eventRepositoryPort.save(event);
    }

    @Override
    public void deleteEvent(String id) {
        eventRepositoryPort.deleteById(id);
    }
}
