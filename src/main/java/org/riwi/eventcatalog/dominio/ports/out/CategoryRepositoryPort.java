package org.riwi.eventcatalog.dominio.ports.out;

import org.riwi.eventcatalog.dominio.model.Category;

import java.util.Optional;

public interface CategoryRepositoryPort {
    Optional<Category> findById(String id);
}