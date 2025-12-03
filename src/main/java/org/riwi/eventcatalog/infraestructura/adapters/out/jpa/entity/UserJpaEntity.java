package org.riwi.eventcatalog.infraestructura.adapters.out.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.riwi.eventcatalog.dominio.model.Role;

@Entity
@Table(name = "app_user")
@Data
public class UserJpaEntity {
    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}
