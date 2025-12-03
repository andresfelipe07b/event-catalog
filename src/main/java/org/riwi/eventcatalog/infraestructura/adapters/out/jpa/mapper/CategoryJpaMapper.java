package org.riwi.eventcatalog.infraestructura.adapters.out.jpa.mapper;

import org.mapstruct.Mapper;
import org.riwi.eventcatalog.dominio.model.Category;
import org.riwi.eventcatalog.infraestructura.adapters.out.jpa.entity.CategoryJpaEntity;

@Mapper(componentModel = "spring")
public interface CategoryJpaMapper {

    Category toDomain(CategoryJpaEntity entity);

    CategoryJpaEntity toEntity(Category domain);
}