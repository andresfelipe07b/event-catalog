package org.riwi.eventcatalog.repository;

import org.riwi.eventcatalog.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, String>, JpaSpecificationExecutor<EventEntity> {
    Optional<EventEntity> findByName(String name);
}
