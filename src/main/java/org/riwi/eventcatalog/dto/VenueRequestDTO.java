package org.riwi.eventcatalog.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VenueRequestDTO {
    @NotBlank(message = "El nombre del lugar no puede estar vacío")
    @Size(min = 3, max = 100, message = "El nombre del lugar debe tener entre 3 y 100 caracteres")
    private String name;

    @NotBlank(message = "La ciudad no puede estar vacía")
    @Size(min = 3, max = 100, message = "La ciudad debe tener entre 3 y 100 caracteres")
    private String city;

    @Min(value = 1, message = "La capacidad debe ser de al menos 1")
    private int capacity;
}
