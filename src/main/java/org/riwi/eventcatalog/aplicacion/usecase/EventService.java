package org.riwi.eventcatalog.aplicacion.usecase;

import lombok.AllArgsConstructor;
import org.riwi.eventcatalog.dominio.exception.IdNotFoundException;
import org.riwi.eventcatalog.dominio.model.Category;
import org.riwi.eventcatalog.dominio.model.Event;
import org.riwi.eventcatalog.dominio.model.EventSearchCriteria;
import org.riwi.eventcatalog.dominio.model.Venue;
import org.riwi.eventcatalog.dominio.ports.in.EventUseCase;
import org.riwi.eventcatalog.dominio.ports.out.CategoryRepositoryPort;
import org.riwi.eventcatalog.dominio.ports.out.EventRepositoryPort;
import org.riwi.eventcatalog.dominio.ports.out.VenueRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class EventService implements EventUseCase {

    private final EventRepositoryPort eventRepositoryPort;
    private final VenueRepositoryPort venueRepositoryPort;
    private final CategoryRepositoryPort categoryRepositoryPort;

    @Override
    public Event createEvent(Event event) {
        eventRepositoryPort.findByName(event.getName()).ifPresent(e -> {
            throw new IllegalArgumentException("El nombre del evento ya existe");
        });

        Venue venue = venueRepositoryPort.findById(event.getVenue().getId())
                .orElseThrow(() -> new IdNotFoundException("Venue"));
        event.setVenue(venue);

        Set<Category> managedCategories = event.getCategories().stream()
                .map(cat -> categoryRepositoryPort.findById(cat.getId())
                        .orElseThrow(() -> new IdNotFoundException("Category con id " + cat.getId())))
                .collect(Collectors.toSet());

        event.setCategories(managedCategories);

        return eventRepositoryPort.save(event);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Event> getEventById(String id) {
        return eventRepositoryPort.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getAllEvents() {
        return eventRepositoryPort.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getAllEventsByCriteria(EventSearchCriteria criteria) {
        return eventRepositoryPort.findAllByCriteria(criteria);
    }

    @Override
    @Transactional
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

        Set<Category> managedCategories = event.getCategories().stream()
                .map(cat -> categoryRepositoryPort.findById(cat.getId())
                        .orElseThrow(() -> new IdNotFoundException("Category con id " + cat.getId())))
                .collect(Collectors.toSet());

        existingEvent.setName(event.getName());
        existingEvent.setDate(event.getDate());
        existingEvent.setDescription(event.getDescription());
        existingEvent.setStatus(event.getStatus());
        existingEvent.setCategories(managedCategories);
        existingEvent.setVenue(venue);

        return eventRepositoryPort.save(existingEvent);
    }

    @Override
    @Transactional
    public void deleteEvent(String id) {
        eventRepositoryPort.findById(id)
                .orElseThrow(() -> new IdNotFoundException("Event"));
        eventRepositoryPort.deleteById(id);
    }
}
