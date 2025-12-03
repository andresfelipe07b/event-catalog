package org.riwi.eventcatalog.infraestructura.config;

import org.riwi.eventcatalog.aplicacion.usecase.EventService;
import org.riwi.eventcatalog.aplicacion.usecase.VenueService;
import org.riwi.eventcatalog.dominio.ports.in.EventUseCase;
import org.riwi.eventcatalog.dominio.ports.in.VenueUseCase;
import org.riwi.eventcatalog.dominio.ports.out.EventRepositoryPort;
import org.riwi.eventcatalog.dominio.ports.out.VenueRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public VenueUseCase venueUseCase(VenueRepositoryPort venueRepositoryPort) {
        return new VenueService(venueRepositoryPort);
    }
}
