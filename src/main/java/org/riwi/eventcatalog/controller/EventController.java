package org.riwi.eventcatalog.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.riwi.eventcatalog.dto.EventRequestDTO;
import org.riwi.eventcatalog.dto.EventResponseDTO;
import org.riwi.eventcatalog.service.IEventService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class EventController {

    private final IEventService eventService;

    @PostMapping
    public ResponseEntity<EventResponseDTO> create(@Valid @RequestBody EventRequestDTO request) {
        return ResponseEntity.ok(eventService.create(request));
    }

    @GetMapping
    public ResponseEntity<Page<EventResponseDTO>> getAll(@ParameterObject Pageable pageable,
                                                       @RequestParam(required = false) String city,
                                                       @RequestParam(required = false) String category,
                                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(eventService.getAll(pageable, city, category, date));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(eventService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> update(@PathVariable String id, @Valid @RequestBody EventRequestDTO request) {
        return ResponseEntity.ok(eventService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
