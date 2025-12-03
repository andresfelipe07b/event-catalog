package org.riwi.eventcatalog.infraestructura.adapters.out.jpa;

import lombok.AllArgsConstructor;
import org.riwi.eventcatalog.dominio.model.Category;
import org.riwi.eventcatalog.dominio.ports.out.CategoryRepositoryPort;
import org.riwi.eventcatalog.infraestructura.adapters.out.jpa.mapper.CategoryJpaMapper;
import org.riwi.eventcatalog.infraestructura.adapters.out.jpa.repository.CategoryJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepositoryPort {

    private final CategoryJpaRepository categoryJpaRepository;
    private final CategoryJpaMapper categoryJpaMapper;

    @Override
    public Optional<Category> findById(String id) {
        return categoryJpaRepository.findById(id).map(categoryJpaMapper::toDomain);
    }
}