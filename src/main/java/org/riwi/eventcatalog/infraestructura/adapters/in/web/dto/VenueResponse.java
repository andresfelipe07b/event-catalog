package org.riwi.eventcatalog.infraestructura.adapters.in.web.dto;

import lombok.Data;

import java.util.List;

@Data
public class VenueResponse {
    private String id;
    private String name;
    private String city;
    private int capacity;
    private List<EventResponse> events;
}
