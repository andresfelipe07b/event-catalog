package org.riwi.eventcatalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.riwi.eventcatalog.dto.VenueDto;
import org.riwi.eventcatalog.exception.ApiError;
import org.riwi.eventcatalog.service.IService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/venues")
public class VenueController {

    private final IService<VenueDto> venueService;

    public VenueController(IService<VenueDto> venueService) {
        this.venueService = venueService;
    }

    @Operation(
            summary = "Create a new venue",
            description = "Registers a new venue in the catalog.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Venue created successfully.",
                            content = @Content(schema = @Schema(implementation = VenueDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid venue data provided.",
                            content = @Content(schema = @Schema(implementation = ApiError.class)))
            }
    )
    @PostMapping
    public ResponseEntity<VenueDto> createVenue(@Valid @RequestBody VenueDto venue) {
        VenueDto createdVenue = venueService.create(venue);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVenue);
    }

    @Operation(
            summary = "Get all venues",
            description = "Retrieves a list of all available venues.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of venues retrieved successfully.",
                            content = @Content(schema = @Schema(implementation = VenueDto.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<VenueDto>> getAllVenues() {
        return ResponseEntity.ok(venueService.findAll());
    }

    @Operation(
            summary = "Get venue by ID",
            description = "Retrieves a specific venue by its unique ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Venue found.",
                            content = @Content(schema = @Schema(implementation = VenueDto.class))),
                    @ApiResponse(responseCode = "404", description = "Venue not found.",
                            content = @Content(schema = @Schema(implementation = ApiError.class)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<VenueDto> getVenueById(@PathVariable Long id) {
        return ResponseEntity.ok(venueService.findById(id));
    }

    @Operation(
            summary = "Update a venue",
            description = "Updates the details of an existing venue by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Venue updated successfully.",
                            content = @Content(schema = @Schema(implementation = VenueDto.class))),
                    @ApiResponse(responseCode = "404", description = "Venue not found.",
                            content = @Content(schema = @Schema(implementation = ApiError.class)))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<VenueDto> updateVenue(@PathVariable Long id, @Valid @RequestBody VenueDto updatedVenue) {
        return ResponseEntity.ok(venueService.update(id, updatedVenue));
    }

    @Operation(
            summary = "Delete a venue",
            description = "Removes a venue from the catalog by its ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Venue deleted successfully."),
                    @ApiResponse(responseCode = "404", description = "Venue not found.",
                            content = @Content(schema = @Schema(implementation = ApiError.class)))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        venueService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
