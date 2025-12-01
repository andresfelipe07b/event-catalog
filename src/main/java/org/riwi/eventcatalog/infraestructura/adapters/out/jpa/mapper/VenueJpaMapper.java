package org.riwi.eventcatalog.infraestructura.adapters.out.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.riwi.eventcatalog.dominio.model.Venue;
import org.riwi.eventcatalog.infraestructura.adapters.out.jpa.entity.VenueJpaEntity;

@Mapper(componentModel = "spring")
public interface VenueJpaMapper {

    @Mapping(target = "events", ignore = true) // Evitar recursividad
    Venue toDomain(VenueJpaEntity entity);

    @Mapping(target = "events", ignore = true) // Evitar recursividad
    VenueJpaEntity toEntity(Venue domain);
}
