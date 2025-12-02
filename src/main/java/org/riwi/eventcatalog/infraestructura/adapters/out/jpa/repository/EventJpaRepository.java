package org.riwi.eventcatalog.infraestructura.adapters.out.jpa.repository;

import org.riwi.eventcatalog.infraestructura.adapters.out.jpa.entity.EventJpaEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventJpaRepository extends JpaRepository<EventJpaEntity, String>, JpaSpecificationExecutor<EventJpaEntity> {
    Optional<EventJpaEntity> findByName(String name);

    @Override
    @EntityGraph(value = "Event.withVenueAndCategories")
    List<EventJpaEntity> findAll();
}
