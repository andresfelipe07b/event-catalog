package org.riwi.eventcatalog.infraestructura.adapters.out.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.riwi.eventcatalog.dominio.model.Event;
import org.riwi.eventcatalog.infraestructura.adapters.out.jpa.entity.EventJpaEntity;

@Mapper(componentModel = "spring", uses = {VenueJpaMapper.class})
public interface EventJpaMapper {

    @Mapping(source = "venue", target = "venue")
    Event toDomain(EventJpaEntity entity);

    @Mapping(source = "venue", target = "venue")
    EventJpaEntity toEntity(Event domain);
}
