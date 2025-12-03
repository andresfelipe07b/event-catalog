package org.riwi.eventcatalog.infraestructura.adapters.in.web.dto;

import lombok.Data;

@Data
public class VenueBasicResponse {
    private String id;
    private String name;
    private String city;
    private int capacity;
}
