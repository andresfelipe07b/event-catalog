package org.riwi.eventcatalog.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestDTO {
    @NotBlank(message = "El nombre del evento no puede estar vacío")
    @Size(min = 3, max = 100, message = "El nombre del evento debe tener entre 3 y 100 caracteres")
    private String name;

    @NotNull(message = "La fecha del evento no puede ser nula")
    @Future(message = "La fecha del evento debe ser en el futuro")
    private LocalDate date;

    @NotBlank(message = "La descripción del evento no puede estar vacía")
    @Size(min = 10, max = 500, message = "La descripción del evento debe tener entre 10 y 500 caracteres")
    private String description;

    @NotBlank(message = "La categoría del evento no puede estar vacía")
    @Size(min = 3, max = 50, message = "La categoría debe tener entre 3 y 50 caracteres")
    private String category;

    @NotBlank(message = "El ID del lugar no puede estar vacío")
    private String venueId;
}
