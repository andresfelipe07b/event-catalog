package org.riwi.eventcatalog.infraestructura.adapters.out.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.riwi.eventcatalog.dominio.model.Event;
import org.riwi.eventcatalog.infraestructura.adapters.out.jpa.entity.EventJpaEntity;

@Mapper(componentModel = "spring", uses = {VenueJpaMapper.class, CategoryJpaMapper.class})
public interface EventJpaMapper {

    @Mapping(source = "venue", target = "venue")
    @Mapping(source = "categories", target = "categories")
    Event toDomain(EventJpaEntity entity);

    @Mapping(source = "venue", target = "venue")
    @Mapping(source = "categories", target = "categories")
    EventJpaEntity toEntity(Event domain);
}
