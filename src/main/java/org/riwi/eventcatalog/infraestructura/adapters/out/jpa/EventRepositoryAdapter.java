package org.riwi.eventcatalog.infraestructura.adapters.out.jpa;

import lombok.AllArgsConstructor;
import org.riwi.eventcatalog.dominio.model.Event;
import org.riwi.eventcatalog.dominio.model.EventSearchCriteria;
import org.riwi.eventcatalog.dominio.ports.out.EventRepositoryPort;
import org.riwi.eventcatalog.infraestructura.adapters.out.jpa.entity.EventJpaEntity;
import org.riwi.eventcatalog.infraestructura.adapters.out.jpa.mapper.EventJpaMapper;
import org.riwi.eventcatalog.infraestructura.adapters.out.jpa.repository.EventJpaRepository;
import org.riwi.eventcatalog.infraestructura.adapters.out.jpa.specification.EventSpecification; // Importar
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class EventRepositoryAdapter implements EventRepositoryPort {

    private final EventJpaRepository eventJpaRepository;
    private final EventJpaMapper eventJpaMapper;
    private final EventSpecification eventSpecification; // Inyectar

    @Override
    public Event save(Event event) {
        EventJpaEntity eventJpaEntity = eventJpaMapper.toEntity(event);
        EventJpaEntity savedEntity = eventJpaRepository.save(eventJpaEntity);
        return eventJpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Event> findById(String id) {
        return eventJpaRepository.findById(id).map(eventJpaMapper::toDomain);
    }

    @Override
    public Optional<Event> findByName(String name) {
        return eventJpaRepository.findByName(name).map(eventJpaMapper::toDomain);
    }

    @Override
    public List<Event> findAll() {
        return eventJpaRepository.findAll().stream()
                .map(eventJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> findAllByCriteria(EventSearchCriteria criteria) {
        return eventJpaRepository.findAll(eventSpecification.getEventsByCriteria(
                        criteria.getCity(), criteria.getCategory(), criteria.getStartDate(), criteria.getEndDate(), criteria.getStatus()))
                .stream()
                .map(eventJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        eventJpaRepository.deleteById(id);
    }
}
