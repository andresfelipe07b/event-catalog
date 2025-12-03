package org.riwi.eventcatalog.infraestructura.adapters.out.jpa.specification;

import org.riwi.eventcatalog.dominio.model.EventStatus;
import org.riwi.eventcatalog.infraestructura.adapters.out.jpa.entity.EventJpaEntity; // Actualizado
import org.riwi.eventcatalog.infraestructura.adapters.out.jpa.entity.VenueJpaEntity; // Actualizado
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class EventSpecification {

    public Specification<EventJpaEntity> getEventsByCriteria(String city, String categoryName, LocalDate startDate, LocalDate endDate, EventStatus status) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (city != null && !city.trim().isEmpty()) {
                Join<EventJpaEntity, VenueJpaEntity> venueJoin = root.join("venue");
                predicates.add(criteriaBuilder.equal(venueJoin.get("city"), city));
            }

            if (categoryName != null && !categoryName.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.join("categories").get("name"), categoryName));
            }

            if (startDate != null && endDate != null) {
                predicates.add(criteriaBuilder.between(root.get("date"), startDate, endDate));
            } else if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), startDate));
            } else if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), endDate));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
