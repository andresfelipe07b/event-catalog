package org.riwi.eventcatalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.riwi.eventcatalog.dto.EventDto;
import org.riwi.eventcatalog.exception.ApiError;
import org.riwi.eventcatalog.service.IService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final IService<EventDto> eventService;

    public EventController(IService<EventDto> eventService) {
        this.eventService = eventService;
    }

    @Operation(
            summary = "Create a new event",
            description = "Registers a new event in the catalog.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Event created successfully.",
                            content = @Content(schema = @Schema(implementation = EventDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid event data provided.",
                            content = @Content(schema = @Schema(implementation = ApiError.class)))
            }
    )
    @PostMapping
    public ResponseEntity<EventDto> createEvent(@Valid @RequestBody EventDto event) {
        EventDto createdEvent = eventService.create(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @Operation(
            summary = "Get all events",
            description = "Retrieves a list of all available events.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of events retrieved successfully.",
                            content = @Content(schema = @Schema(implementation = EventDto.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<EventDto>> getAllEvents() {
        return ResponseEntity.ok(eventService.findAll());
    }

    @Operation(
            summary = "Get event by ID",
            description = "Retrieves a specific event by its unique ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Event found.",
                            content = @Content(schema = @Schema(implementation = EventDto.class))),
                    @ApiResponse(responseCode = "404", description = "Event not found.",
                            content = @Content(schema = @Schema(implementation = ApiError.class)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.findById(id));
    }

    @Operation(
            summary = "Update an event",
            description = "Updates the details of an existing event by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Event updated successfully.",
                            content = @Content(schema = @Schema(implementation = EventDto.class))),
                    @ApiResponse(responseCode = "404", description = "Event not found.",
                            content = @Content(schema = @Schema(implementation = ApiError.class)))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable Long id, @Valid @RequestBody EventDto updatedEvent) {
        return ResponseEntity.ok(eventService.update(id, updatedEvent));
    }

    @Operation(
            summary = "Delete an event",
            description = "Removes an event from the catalog by its ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Event deleted successfully."),
                    @ApiResponse(responseCode = "404", description = "Event not found.",
                            content = @Content(schema = @Schema(implementation = ApiError.class)))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
