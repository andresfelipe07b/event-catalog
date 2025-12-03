package org.riwi.eventcatalog.infraestructura.adapters.in.web;

import lombok.AllArgsConstructor;
import org.riwi.eventcatalog.dominio.exception.IdNotFoundException;
import org.riwi.eventcatalog.dominio.model.Event;
import org.riwi.eventcatalog.dominio.model.EventSearchCriteria;
import org.riwi.eventcatalog.dominio.ports.in.EventUseCase;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.EventRequest;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.EventResponse;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.mapper.EventDtoMapper;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.validation.groups.OnCreate;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.validation.groups.OnUpdate;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class EventController {

    private final EventUseCase eventUseCase;
    private final EventDtoMapper eventDtoMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponse> create(@Validated(OnCreate.class) @RequestBody EventRequest request) {
        Event event = eventDtoMapper.toDomain(request);
        Event createdEvent = eventUseCase.createEvent(event);
        return ResponseEntity.ok(eventDtoMapper.toResponse(createdEvent));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<EventResponse>> getAll(@ParameterObject EventSearchCriteria criteria) {
        List<Event> events = eventUseCase.getAllEventsByCriteria(criteria);
        List<EventResponse> response = events.stream()
                .map(eventDtoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<EventResponse> getById(@PathVariable String id) {
        Event event = eventUseCase.getEventById(id)
                .orElseThrow(() -> new IdNotFoundException("Event con id " + id + " no encontrado"));
        return ResponseEntity.ok(eventDtoMapper.toResponse(event));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponse> update(@PathVariable String id, @Validated(OnUpdate.class) @RequestBody EventRequest request) {
        Event event = eventDtoMapper.toDomain(request);
        Event updatedEvent = eventUseCase.updateEvent(id, event);
        return ResponseEntity.ok(eventDtoMapper.toResponse(updatedEvent));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        eventUseCase.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
