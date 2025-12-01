package org.riwi.eventcatalog.infraestructura.adapters.in.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.riwi.eventcatalog.dominio.model.Venue;
import org.riwi.eventcatalog.dominio.ports.in.VenueUseCase;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.VenueRequest;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.VenueResponse;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.mapper.VenueDtoMapper;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<VenueResponse> create(@Valid @RequestBody VenueRequest request) {
        Venue venue = venueDtoMapper.toDomain(request);
        Venue createdVenue = venueUseCase.createVenue(venue);
        return ResponseEntity.ok(venueDtoMapper.toResponse(createdVenue));
    }

    @GetMapping
    public ResponseEntity<List<VenueResponse>> getAll() {
        List<Venue> venues = venueUseCase.getAllVenues();
        List<VenueResponse> response = venues.stream()
                .map(venueDtoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueResponse> getById(@PathVariable String id) {
        return venueUseCase.getVenueById(id)
                .map(venueDtoMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VenueResponse> update(@PathVariable String id, @Valid @RequestBody VenueRequest request) {
        Venue venue = venueDtoMapper.toDomain(request);
        Venue updatedVenue = venueUseCase.updateVenue(id, venue);
        return ResponseEntity.ok(venueDtoMapper.toResponse(updatedVenue));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        venueUseCase.deleteVenue(id);
        return ResponseEntity.noContent().build();
    }
}
