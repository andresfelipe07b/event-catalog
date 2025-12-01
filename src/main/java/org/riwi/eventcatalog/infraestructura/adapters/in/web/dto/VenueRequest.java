package org.riwi.eventcatalog.infraestructura.adapters.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VenueRequest {
    @NotBlank
    private String name;
    private String city;
    private int capacity;
}
