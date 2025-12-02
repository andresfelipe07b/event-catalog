package org.riwi.eventcatalog.infraestructura.adapters.out.jpa.repository;

import org.riwi.eventcatalog.infraestructura.adapters.out.jpa.entity.CategoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryJpaRepository extends JpaRepository<CategoryJpaEntity, String> {
}