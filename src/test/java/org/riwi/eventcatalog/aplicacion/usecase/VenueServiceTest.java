package org.riwi.eventcatalog.aplicacion.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.riwi.eventcatalog.dominio.exception.IdNotFoundException;
import org.riwi.eventcatalog.dominio.model.Venue;
import org.riwi.eventcatalog.dominio.ports.out.VenueRepositoryPort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VenueServiceTest {

    @Mock
    private VenueRepositoryPort venueRepositoryPort;

    @InjectMocks
    private VenueService venueService;

    private Venue sampleVenue;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleVenue = new Venue("venue-id", "Test Venue", "Test City", 1000, Collections.emptyList());
    }

    @Test
    void createVenue_shouldCreateSuccessfully() {
        // Given
        when(venueRepositoryPort.save(any(Venue.class))).thenReturn(sampleVenue);

        // When
        Venue createdVenue = venueService.createVenue(sampleVenue);

        // Then
        assertNotNull(createdVenue);
        assertEquals(sampleVenue.getName(), createdVenue.getName());
        verify(venueRepositoryPort, times(1)).save(sampleVenue);
    }

    @Test
    void getVenueById_shouldReturnVenueIfExists() {
        // Given
        when(venueRepositoryPort.findById("venue-id")).thenReturn(Optional.of(sampleVenue));

        // When
        Optional<Venue> foundVenue = venueService.getVenueById("venue-id");

        // Then
        assertTrue(foundVenue.isPresent());
        assertEquals(sampleVenue.getName(), foundVenue.get().getName());
        verify(venueRepositoryPort, times(1)).findById("venue-id");
    }

    @Test
    void getVenueById_shouldReturnEmptyIfNotFound() {
        // Given
        when(venueRepositoryPort.findById("non-existent-id")).thenReturn(Optional.empty());

        // When
        Optional<Venue> foundVenue = venueService.getVenueById("non-existent-id");

        // Then
        assertFalse(foundVenue.isPresent());
        verify(venueRepositoryPort, times(1)).findById("non-existent-id");
    }

    @Test
    void getAllVenues_shouldReturnListOfVenues() {
        // Given
        when(venueRepositoryPort.findAll()).thenReturn(List.of(sampleVenue));

        // When
        List<Venue> venues = venueService.getAllVenues();

        // Then
        assertFalse(venues.isEmpty());
        assertEquals(1, venues.size());
        verify(venueRepositoryPort, times(1)).findAll();
    }

    @Test
    void updateVenue_shouldUpdateSuccessfully() {
        // Given
        Venue updatedVenueDetails = new Venue("venue-id", "Updated Venue", "Updated City", 1200, Collections.emptyList());
        when(venueRepositoryPort.findById("venue-id")).thenReturn(Optional.of(sampleVenue));
        when(venueRepositoryPort.save(any(Venue.class))).thenReturn(updatedVenueDetails);

        // When
        Venue result = venueService.updateVenue("venue-id", updatedVenueDetails);

        // Then
        assertNotNull(result);
        assertEquals("Updated Venue", result.getName());
        assertEquals("Updated City", result.getCity());
        verify(venueRepositoryPort, times(1)).findById("venue-id");
        verify(venueRepositoryPort, times(1)).save(any(Venue.class));
    }

    @Test
    void updateVenue_shouldThrowExceptionIfVenueNotFound() {
        // Given
        when(venueRepositoryPort.findById("non-existent-id")).thenReturn(Optional.empty());

        // When & Then
        IdNotFoundException thrown = assertThrows(IdNotFoundException.class, () -> {
            venueService.updateVenue("non-existent-id", sampleVenue);
        });
        assertEquals("No se encontró un registro en la entidad Venue con el ID proporcionado.", thrown.getMessage()); // Adjusted message
        verify(venueRepositoryPort, times(1)).findById("non-existent-id");
        verify(venueRepositoryPort, never()).save(any(Venue.class));
    }

    @Test
    void deleteVenue_shouldDeleteSuccessfully() {
        // Given
        when(venueRepositoryPort.findById("venue-id")).thenReturn(Optional.of(sampleVenue)); // Mock findById for delete logic
        doNothing().when(venueRepositoryPort).deleteById("venue-id");

        // When
        venueService.deleteVenue("venue-id");

        // Then
        verify(venueRepositoryPort, times(1)).findById("venue-id");
        verify(venueRepositoryPort, times(1)).deleteById("venue-id");
    }

    @Test
    void deleteVenue_shouldThrowExceptionIfVenueNotFound() {
        // Given
        when(venueRepositoryPort.findById("non-existent-id")).thenReturn(Optional.empty());

        // When & Then
        IdNotFoundException thrown = assertThrows(IdNotFoundException.class, () -> {
            venueService.deleteVenue("non-existent-id");
        });
        assertEquals("No se encontró un registro en la entidad Venue con el ID proporcionado.", thrown.getMessage()); // Adjusted message
        verify(venueRepositoryPort, times(1)).findById("non-existent-id");
        verify(venueRepositoryPort, never()).deleteById(anyString());
    }
}
