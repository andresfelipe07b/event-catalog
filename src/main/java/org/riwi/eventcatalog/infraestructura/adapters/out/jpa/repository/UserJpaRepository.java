package org.riwi.eventcatalog.infraestructura.adapters.out.jpa.repository;

import org.riwi.eventcatalog.infraestructura.adapters.out.jpa.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, String> {
    Optional<UserJpaEntity> findByUsername(String username);
}
