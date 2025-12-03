package org.riwi.eventcatalog.infraestructura.adapters.in.web.mapper;

import org.mapstruct.Mapper;
import org.riwi.eventcatalog.dominio.model.Category;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.CategoryRequest;

@Mapper(componentModel = "spring")
public interface CategoryDtoMapper {
    Category toDomain(CategoryRequest request);
}
