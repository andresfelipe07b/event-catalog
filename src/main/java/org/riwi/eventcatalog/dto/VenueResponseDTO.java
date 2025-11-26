package org.riwi.eventcatalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VenueResponseDTO {
    private String id;
    private String name;
    private String city;
    private int capacity;
    private List<EventResponseDTO> events;
}
