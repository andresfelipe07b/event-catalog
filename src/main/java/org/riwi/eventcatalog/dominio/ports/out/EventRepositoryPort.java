package org.riwi.eventcatalog.dominio.ports.out;

import org.riwi.eventcatalog.dominio.model.Event;
import org.riwi.eventcatalog.dominio.model.EventSearchCriteria;

import java.util.List;
import java.util.Optional;

public interface EventRepositoryPort {
    Event save(Event event);
    Optional<Event> findById(String id);
    Optional<Event> findByName(String name); // Añadido
    List<Event> findAll();
    List<Event> findAllByCriteria(EventSearchCriteria criteria);
    void deleteById(String id);
}
