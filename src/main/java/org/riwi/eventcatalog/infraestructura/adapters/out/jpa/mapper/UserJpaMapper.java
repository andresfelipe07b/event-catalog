package org.riwi.eventcatalog.infraestructura.adapters.out.jpa.mapper;

import org.mapstruct.Mapper;
import org.riwi.eventcatalog.dominio.model.User;
import org.riwi.eventcatalog.infraestructura.adapters.out.jpa.entity.UserJpaEntity;

@Mapper(componentModel = "spring")
public interface UserJpaMapper {
    User toDomain(UserJpaEntity entity);
    UserJpaEntity toEntity(User domain);
}
