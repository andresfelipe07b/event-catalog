package org.riwi.eventcatalog.infraestructura.adapters.in.web.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EventResponse {
    private String id;
    private String name;
    private LocalDate date;
    private String description;
    private String category;
    private VenueBasicResponse venue; // Cambiado a VenueBasicResponse
}
