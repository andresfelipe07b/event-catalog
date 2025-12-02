package org.riwi.eventcatalog.infraestructura.adapters.out.jpa.entity;

import jakarta.persistence.*;
import org.riwi.eventcatalog.dominio.model.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity
@NamedEntityGraph(
    name = "Event.withVenueAndCategories",
    attributeNodes = {
        @NamedAttributeNode("venue"),
        @NamedAttributeNode("categories")
    }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "event")
public class EventJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false, unique = true)
    private String name;
    private LocalDate date;
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "event_category",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<CategoryJpaEntity> categories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id")
    private VenueJpaEntity venue;
}
