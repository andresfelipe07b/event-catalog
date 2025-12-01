package org.riwi.eventcatalog.infraestructura.adapters.in.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.riwi.eventcatalog.dominio.model.Venue;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.VenueRequest;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.VenueResponse;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.VenueBasicResponse;

@Mapper(componentModel = "spring", uses = {EventDtoMapper.class})
public interface VenueDtoMapper {
    Venue toDomain(VenueRequest request);

    @Mapping(target = "events", ignore = true)
    VenueResponse toResponse(Venue domain);

    @Named("toVenueBasicResponse") // Añadido para ser referenciado por EventDtoMapper
    VenueBasicResponse toBasicResponse(Venue domain);
}
