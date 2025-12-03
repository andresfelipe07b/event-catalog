package org.riwi.eventcatalog.dominio.ports.in;

import org.riwi.eventcatalog.dominio.model.Event;
import org.riwi.eventcatalog.dominio.model.EventSearchCriteria; // Importar

import java.util.List;
import java.util.Optional;

public interface EventUseCase {
    Event createEvent(Event event);
    Optional<Event> getEventById(String id);
    List<Event> getAllEvents();
    List<Event> getAllEventsByCriteria(EventSearchCriteria criteria); // Añadido
    Event updateEvent(String id, Event event);
    void deleteEvent(String id);
}
