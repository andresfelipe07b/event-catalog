package org.riwi.eventcatalog.infraestructura.adapters.in.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.riwi.eventcatalog.dominio.model.EventStatus;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.validation.groups.OnCreate;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.validation.groups.OnUpdate;

import java.time.LocalDate;
import java.util.Set;

@Data
public class EventRequest {
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private LocalDate date;

    private String description;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private EventStatus status;

    @Valid
    @NotEmpty(groups = {OnCreate.class, OnUpdate.class})
    private Set<CategoryRequest> categories;

    @Valid
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private VenueRequest venue;

    @Data
    public static class VenueRequest {
        @NotBlank(groups = {OnCreate.class, OnUpdate.class})
        private String id;
    }
}
