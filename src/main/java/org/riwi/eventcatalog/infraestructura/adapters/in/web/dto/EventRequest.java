package org.riwi.eventcatalog.infraestructura.adapters.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EventRequest {
    @NotBlank
    private String name;
    private LocalDate date;
    private String description;
    @NotBlank
    private String category;
    @NotBlank
    private String venueId;
}
