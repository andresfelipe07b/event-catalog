package org.riwi.eventcatalog.infraestructura.adapters.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.riwi.eventcatalog.dominio.model.Role;

@Data
public class RegisterRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private Role role = Role.USER; // Rol por defecto
}
