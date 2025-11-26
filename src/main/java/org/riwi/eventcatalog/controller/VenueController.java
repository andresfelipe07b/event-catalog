package org.riwi.eventcatalog.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.riwi.eventcatalog.dto.VenueBasicResponseDTO;
import org.riwi.eventcatalog.dto.VenueRequestDTO;
import org.riwi.eventcatalog.dto.VenueResponseDTO;
import org.riwi.eventcatalog.service.IVenueService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/venues")
@AllArgsConstructor
public class VenueController {

    private final IVenueService venueService;

    @PostMapping
    public ResponseEntity<VenueResponseDTO> create(@Valid @RequestBody VenueRequestDTO request) {
        return ResponseEntity.ok(venueService.create(request));
    }

    @GetMapping
    public ResponseEntity<Page<VenueBasicResponseDTO>> getAll(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(venueService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(venueService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VenueResponseDTO> update(@PathVariable String id, @Valid @RequestBody VenueRequestDTO request) {
        return ResponseEntity.ok(venueService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        venueService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
