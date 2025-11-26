package org.riwi.eventcatalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventResponseDTO {
    private String id;
    private String name;
    private LocalDate date;
    private String description;
    private String category;
    private VenueBasicResponseDTO venue;
}
