package org.riwi.eventcatalog.aplicacion.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.riwi.eventcatalog.dominio.exception.IdNotFoundException;
import org.riwi.eventcatalog.dominio.model.*;
import org.riwi.eventcatalog.dominio.ports.out.CategoryRepositoryPort;
import org.riwi.eventcatalog.dominio.ports.out.EventRepositoryPort;
import org.riwi.eventcatalog.dominio.ports.out.VenueRepositoryPort;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventRepositoryPort eventRepositoryPort;
    @Mock
    private VenueRepositoryPort venueRepositoryPort;
    @Mock
    private CategoryRepositoryPort categoryRepositoryPort;

    @InjectMocks
    private EventService eventService;

    private Event sampleEvent;
    private Venue sampleVenue;
    private Category sampleCategory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleVenue = new Venue("venue-id", "Sample Venue", "City", 1000, Collections.emptyList());
        sampleCategory = new Category("cat-id", "Music");
        Set<Category> categories = new HashSet<>();
        categories.add(sampleCategory);

        sampleEvent = new Event("event-id", "Sample Event", LocalDate.now(), "Description", EventStatus.ACTIVE, categories, sampleVenue);
    }

    @Test
    void createEvent_shouldCreateSuccessfully() {
        // Given
        when(eventRepositoryPort.findByName(anyString())).thenReturn(Optional.empty());
        when(venueRepositoryPort.findById("venue-id")).thenReturn(Optional.of(sampleVenue));
        when(categoryRepositoryPort.findById("cat-id")).thenReturn(Optional.of(sampleCategory));
        when(eventRepositoryPort.save(any(Event.class))).thenReturn(sampleEvent);

        // When
        Event createdEvent = eventService.createEvent(sampleEvent);

        // Then
        assertNotNull(createdEvent);
        assertEquals(sampleEvent.getName(), createdEvent.getName());
        verify(eventRepositoryPort, times(1)).findByName(sampleEvent.getName());
        verify(venueRepositoryPort, times(1)).findById(sampleVenue.getId());
        verify(categoryRepositoryPort, times(1)).findById(sampleCategory.getId());
        verify(eventRepositoryPort, times(1)).save(sampleEvent);
    }

    @Test
    void createEvent_shouldThrowExceptionIfNameExists() {
        // Given
        when(eventRepositoryPort.findByName(anyString())).thenReturn(Optional.of(sampleEvent));

        // When & Then
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            eventService.createEvent(sampleEvent);
        });
        assertEquals("El nombre del evento ya existe", thrown.getMessage());
        verify(eventRepositoryPort, times(1)).findByName(sampleEvent.getName());
        verify(venueRepositoryPort, never()).findById(anyString());
        verify(eventRepositoryPort, never()).save(any(Event.class));
    }

    @Test
    void createEvent_shouldThrowExceptionIfVenueNotFound() {
        // Given
        when(eventRepositoryPort.findByName(anyString())).thenReturn(Optional.empty());
        when(venueRepositoryPort.findById(anyString())).thenReturn(Optional.empty());

        // When & Then
        IdNotFoundException thrown = assertThrows(IdNotFoundException.class, () -> {
            eventService.createEvent(sampleEvent);
        });
        assertEquals("No se encontró un registro en la entidad Venue con el ID proporcionado.", thrown.getMessage()); // Adjusted message
        verify(venueRepositoryPort, times(1)).findById(sampleVenue.getId());
        verify(categoryRepositoryPort, never()).findById(anyString());
        verify(eventRepositoryPort, never()).save(any(Event.class));
    }

    @Test
    void createEvent_shouldThrowExceptionIfCategoryNotFound() {
        // Given
        when(eventRepositoryPort.findByName(anyString())).thenReturn(Optional.empty());
        when(venueRepositoryPort.findById(anyString())).thenReturn(Optional.of(sampleVenue));
        when(categoryRepositoryPort.findById(anyString())).thenReturn(Optional.empty());

        // When & Then
        IdNotFoundException thrown = assertThrows(IdNotFoundException.class, () -> {
            eventService.createEvent(sampleEvent);
        });
        // Use assertEquals for exact match with the dynamically constructed message
        String expectedEntityNameInException = "Category con id " + sampleCategory.getId();
        String expectedFullMessage = "No se encontró un registro en la entidad " + expectedEntityNameInException + " con el ID proporcionado.";
        assertEquals(expectedFullMessage, thrown.getMessage());

        verify(categoryRepositoryPort, times(1)).findById(sampleCategory.getId());
        verify(eventRepositoryPort, never()).save(any(Event.class));
    }

    @Test
    void getEventById_shouldReturnEventIfExists() {
        // Given
        when(eventRepositoryPort.findById("event-id")).thenReturn(Optional.of(sampleEvent));

        // When
        Optional<Event> foundEvent = eventService.getEventById("event-id");

        // Then
        assertTrue(foundEvent.isPresent());
        assertEquals(sampleEvent.getName(), foundEvent.get().getName());
        verify(eventRepositoryPort, times(1)).findById("event-id");
    }

    @Test
    void getEventById_shouldReturnEmptyIfNotFound() {
        // Given
        when(eventRepositoryPort.findById("non-existent-id")).thenReturn(Optional.empty());

        // When
        Optional<Event> foundEvent = eventService.getEventById("non-existent-id");

        // Then
        assertFalse(foundEvent.isPresent());
        verify(eventRepositoryPort, times(1)).findById("non-existent-id");
    }

    @Test
    void getAllEvents_shouldReturnListOfEvents() {
        // Given
        when(eventRepositoryPort.findAll()).thenReturn(List.of(sampleEvent));

        // When
        List<Event> events = eventService.getAllEvents();

        // Then
        assertFalse(events.isEmpty());
        assertEquals(1, events.size());
        verify(eventRepositoryPort, times(1)).findAll();
    }

    @Test
    void getAllEventsByCriteria_shouldReturnFilteredListOfEvents() {
        // Given
        EventSearchCriteria criteria = new EventSearchCriteria();
        criteria.setCity("City");
        when(eventRepositoryPort.findAllByCriteria(criteria)).thenReturn(List.of(sampleEvent));

        // When
        List<Event> events = eventService.getAllEventsByCriteria(criteria);

        // Then
        assertFalse(events.isEmpty());
        assertEquals(1, events.size());
        verify(eventRepositoryPort, times(1)).findAllByCriteria(criteria);
    }

    @Test
    void updateEvent_shouldUpdateSuccessfully() {
        // Given
        Event updatedEventDetails = new Event("event-id", "Updated Event", LocalDate.now().plusDays(1), "New Description", EventStatus.POSTPONED, new HashSet<>(Set.of(sampleCategory)), sampleVenue);
        updatedEventDetails.setId("event-id"); // Ensure ID is set for update logic

        when(eventRepositoryPort.findById("event-id")).thenReturn(Optional.of(sampleEvent));
        when(eventRepositoryPort.findByName(updatedEventDetails.getName())).thenReturn(Optional.empty()); // No conflict with new name
        when(venueRepositoryPort.findById(sampleVenue.getId())).thenReturn(Optional.of(sampleVenue));
        when(categoryRepositoryPort.findById(sampleCategory.getId())).thenReturn(Optional.of(sampleCategory));
        when(eventRepositoryPort.save(any(Event.class))).thenReturn(updatedEventDetails);

        // When
        Event result = eventService.updateEvent("event-id", updatedEventDetails);

        // Then
        assertNotNull(result);
        assertEquals("Updated Event", result.getName());
        assertEquals(EventStatus.POSTPONED, result.getStatus());
        verify(eventRepositoryPort, times(1)).findById("event-id");
        verify(eventRepositoryPort, times(1)).findByName(updatedEventDetails.getName());
        verify(venueRepositoryPort, times(1)).findById(sampleVenue.getId());
        verify(categoryRepositoryPort, times(1)).findById(sampleCategory.getId());
        verify(eventRepositoryPort, times(1)).save(any(Event.class)); // Verifies save was called with the updated existingEvent
    }

    @Test
    void updateEvent_shouldThrowExceptionIfEventNotFound() {
        // Given
        when(eventRepositoryPort.findById("non-existent-id")).thenReturn(Optional.empty());

        // When & Then
        IdNotFoundException thrown = assertThrows(IdNotFoundException.class, () -> {
            eventService.updateEvent("non-existent-id", sampleEvent);
        });
        assertEquals("No se encontró un registro en la entidad Event con el ID proporcionado.", thrown.getMessage()); // Adjusted message
        verify(eventRepositoryPort, times(1)).findById("non-existent-id");
        verify(eventRepositoryPort, never()).save(any(Event.class));
    }

    @Test
    void updateEvent_shouldThrowExceptionIfNewNameConflicts() {
        // Given
        Event existingEventWithDifferentId = new Event("another-id", "Updated Event", LocalDate.now(), "Desc", EventStatus.ACTIVE, new HashSet<>(), sampleVenue);
        Event updatedEventDetails = new Event("event-id", "Updated Event", LocalDate.now().plusDays(1), "New Description", EventStatus.POSTPONED, new HashSet<>(Set.of(sampleCategory)), sampleVenue);

        when(eventRepositoryPort.findById("event-id")).thenReturn(Optional.of(sampleEvent));
        when(eventRepositoryPort.findByName(updatedEventDetails.getName())).thenReturn(Optional.of(existingEventWithDifferentId)); // Conflict

        // When & Then
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            eventService.updateEvent("event-id", updatedEventDetails);
        });
        assertEquals("El nombre del evento ya existe", thrown.getMessage());
        verify(eventRepositoryPort, times(1)).findById("event-id");
        verify(eventRepositoryPort, times(1)).findByName(updatedEventDetails.getName());
        verify(eventRepositoryPort, never()).save(any(Event.class));
    }

    @Test
    void deleteEvent_shouldDeleteSuccessfully() {
        // Given
        when(eventRepositoryPort.findById("event-id")).thenReturn(Optional.of(sampleEvent));
        doNothing().when(eventRepositoryPort).deleteById("event-id");

        // When
        eventService.deleteEvent("event-id");

        // Then
        verify(eventRepositoryPort, times(1)).findById("event-id");
        verify(eventRepositoryPort, times(1)).deleteById("event-id");
    }

    @Test
    void deleteEvent_shouldThrowExceptionIfEventNotFound() {
        // Given
        when(eventRepositoryPort.findById("non-existent-id")).thenReturn(Optional.empty());

        // When & Then
        IdNotFoundException thrown = assertThrows(IdNotFoundException.class, () -> {
            eventService.deleteEvent("non-existent-id");
        });
        assertEquals("No se encontró un registro en la entidad Event con el ID proporcionado.", thrown.getMessage()); // Adjusted message
        verify(eventRepositoryPort, times(1)).findById("non-existent-id");
        verify(eventRepositoryPort, never()).deleteById(anyString());
    }
}
