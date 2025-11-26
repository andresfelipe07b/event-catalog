package org.riwi.eventcatalog.specification;

import org.riwi.eventcatalog.entity.EventEntity;
import org.riwi.eventcatalog.entity.VenueEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class EventSpecification {

    public Specification<EventEntity> getEventsByCriteria(String city, String category, LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            // Lista para almacenar los predicados (condiciones)
            List<Predicate> predicates = new ArrayList<>();

            // 1. Condición para la ciudad (city)
            if (city != null && !city.trim().isEmpty()) {
                Join<EventEntity, VenueEntity> venueJoin = root.join("venue");
                predicates.add(criteriaBuilder.equal(venueJoin.get("city"), city));
            }

            // 2. Condición para la categoría (category)
            if (category != null && !category.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("category"), category));
            }

            // 3. Condición para la fecha (date)
            if (date != null) {
                predicates.add(criteriaBuilder.equal(root.get("date"), date));
            }

            // Combina todos los predicados con un "AND"
            // El metodo toArray convierte la lista de predicados en un array,
            // que es lo que el metodo "and" espera.
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
