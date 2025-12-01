package org.riwi.eventcatalog.infraestructura.adapters.in.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.riwi.eventcatalog.dominio.model.Event;
import org.riwi.eventcatalog.dominio.model.EventSearchCriteria;
import org.riwi.eventcatalog.dominio.ports.in.EventUseCase;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.EventRequest;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.EventResponse;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.mapper.EventDtoMapper;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable; // Necesario para @ParameterObject Pageable
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class EventController {

    private final EventUseCase eventUseCase;
    private final EventDtoMapper eventDtoMapper;

    @PostMapping
    public ResponseEntity<EventResponse> create(@Valid @RequestBody EventRequest request) {
        Event event = eventDtoMapper.toDomain(request);
        Event createdEvent = eventUseCase.createEvent(event);
        return ResponseEntity.ok(eventDtoMapper.toResponse(createdEvent));
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAll(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        EventSearchCriteria criteria = new EventSearchCriteria(city, category, date);
        List<Event> events = eventUseCase.getAllEventsByCriteria(criteria);
        List<EventResponse> response = events.stream()
                .map(eventDtoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getById(@PathVariable String id) {
        return eventUseCase.getEventById(id)
                .map(eventDtoMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> update(@PathVariable String id, @Valid @RequestBody EventRequest request) {
        Event event = eventDtoMapper.toDomain(request);
        Event updatedEvent = eventUseCase.updateEvent(id, event);
        return ResponseEntity.ok(eventDtoMapper.toResponse(updatedEvent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        eventUseCase.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
