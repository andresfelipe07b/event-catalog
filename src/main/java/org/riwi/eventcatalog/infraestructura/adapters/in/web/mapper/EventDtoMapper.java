package org.riwi.eventcatalog.infraestructura.adapters.in.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.riwi.eventcatalog.dominio.model.Event;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.EventRequest;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.EventResponse;

@Mapper(componentModel = "spring", uses = {VenueDtoMapper.class, CategoryDtoMapper.class})
public interface EventDtoMapper {

    @Mapping(source = "venue.id", target = "venue.id")
    Event toDomain(EventRequest request);

    EventResponse toResponse(Event event);
}
