package org.riwi.eventcatalog.service.impl;

import lombok.AllArgsConstructor;
import org.riwi.eventcatalog.dto.EventRequestDTO;
import org.riwi.eventcatalog.dto.EventResponseDTO;
import org.riwi.eventcatalog.dto.VenueBasicResponseDTO;
import org.riwi.eventcatalog.entity.EventEntity;
import org.riwi.eventcatalog.entity.VenueEntity;
import org.riwi.eventcatalog.exception.IdNotFoundException;
import org.riwi.eventcatalog.repository.EventRepository;
import org.riwi.eventcatalog.repository.VenueRepository;
import org.riwi.eventcatalog.service.IEventService;
import org.riwi.eventcatalog.specification.EventSpecification;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class EventServiceImpl implements IEventService {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final EventSpecification eventSpecification;

    @Override
    public EventResponseDTO create(EventRequestDTO request) {
        eventRepository.findByName(request.getName()).ifPresent(event -> {
            throw new IllegalArgumentException("El nombre del evento ya existe");
        });

        VenueEntity venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(() -> new IdNotFoundException("Venue"));

        EventEntity eventToSave = new EventEntity();
        BeanUtils.copyProperties(request, eventToSave);
        eventToSave.setVenue(venue);

        EventEntity savedEvent = eventRepository.save(eventToSave);

        return entityToResponse(savedEvent);
    }

    @Override
    public Page<EventResponseDTO> getAll(Pageable pageable, String city, String category, LocalDate date) {
        Specification<EventEntity> spec = eventSpecification.getEventsByCriteria(city, category, date);
        return eventRepository.findAll(spec, pageable).map(this::entityToResponse);
    }

    @Override
    public EventResponseDTO findById(String id) {
        EventEntity event = eventRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("Event"));
        return entityToResponse(event);
    }

    @Override
    public EventResponseDTO update(String id, EventRequestDTO request) {
        EventEntity eventToUpdate = eventRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("Event"));

        eventRepository.findByName(request.getName()).ifPresent(event -> {
            if (!event.getId().equals(id)) {
                throw new IllegalArgumentException("El nombre del evento ya existe");
            }
        });

        VenueEntity venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(() -> new IdNotFoundException("Venue"));

        BeanUtils.copyProperties(request, eventToUpdate);
        eventToUpdate.setVenue(venue);

        EventEntity updatedEvent = eventRepository.save(eventToUpdate);

        return entityToResponse(updatedEvent);
    }

    @Override
    public void delete(String id) {
        eventRepository.deleteById(id);
    }

    private EventResponseDTO entityToResponse(EventEntity entity) {
        EventResponseDTO response = new EventResponseDTO();
        BeanUtils.copyProperties(entity, response);

        VenueBasicResponseDTO venueDTO = new VenueBasicResponseDTO();
        BeanUtils.copyProperties(entity.getVenue(), venueDTO);
        response.setVenue(venueDTO);

        return response;
    }
}
