package org.riwi.eventcatalog.infraestructura.adapters.in.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.riwi.eventcatalog.dominio.model.Event;
import org.riwi.eventcatalog.dominio.model.Venue;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.EventRequest;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.EventResponse;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.VenueBasicResponse; // Importar

@Mapper(componentModel = "spring", uses = {VenueDtoMapper.class})
public interface EventDtoMapper {
    @Mapping(source = "venueId", target = "venue") // Mapear venueId a un objeto Venue
    Event toDomain(EventRequest request);

    @Mapping(source = "venue", target = "venue", qualifiedByName = "toVenueBasicResponse") // Mapear a VenueBasicResponse
    EventResponse toResponse(Event domain);

    // Metodo para mapear solo el ID del Venue a un objeto Venue del dominio
    default Venue mapVenueIdToVenue(String venueId) {
        if (venueId == null) {
            return null;
        }
        Venue venue = new Venue();
        venue.setId(venueId);
        return venue;
    }
}
