package org.riwi.eventcatalog.infraestructura.adapters.in.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.riwi.eventcatalog.dominio.exception.IdNotFoundException;
import org.riwi.eventcatalog.dominio.model.Venue;
import org.riwi.eventcatalog.dominio.ports.in.VenueUseCase;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.VenueRequest;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.VenueResponse;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.mapper.VenueDtoMapper;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.validation.groups.OnCreate;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.validation.groups.OnUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/venues")
@AllArgsConstructor
public class VenueController {

    private final VenueUseCase venueUseCase;
    private final VenueDtoMapper venueDtoMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VenueResponse> create(@Validated(OnCreate.class) @RequestBody VenueRequest request) {
        Venue venue = venueDtoMapper.toDomain(request);
        Venue createdVenue = venueUseCase.createVenue(venue);
        return ResponseEntity.ok(venueDtoMapper.toResponse(createdVenue));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<VenueResponse>> getAll() {
        List<Venue> venues = venueUseCase.getAllVenues();
        List<VenueResponse> response = venues.stream()
                .map(venueDtoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<VenueResponse> getById(@PathVariable String id) {
        Venue venue = venueUseCase.getVenueById(id)
                .orElseThrow(() -> new IdNotFoundException("Venue con id " + id + " no encontrado"));
        return ResponseEntity.ok(venueDtoMapper.toResponse(venue));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VenueResponse> update(@PathVariable String id, @Validated(OnUpdate.class) @RequestBody VenueRequest request) {
        Venue venue = venueDtoMapper.toDomain(request);
        Venue updatedVenue = venueUseCase.updateVenue(id, venue);
        return ResponseEntity.ok(venueDtoMapper.toResponse(updatedVenue));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        venueUseCase.deleteVenue(id);
        return ResponseEntity.noContent().build();
    }
}
