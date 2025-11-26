package org.riwi.eventcatalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VenueBasicResponseDTO {
    private String id;
    private String name;
    private String city;
    private int capacity;
}
