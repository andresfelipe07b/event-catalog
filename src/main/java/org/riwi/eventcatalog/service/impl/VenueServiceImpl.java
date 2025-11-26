package org.riwi.eventcatalog.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.riwi.eventcatalog.dto.*;
import org.riwi.eventcatalog.entity.VenueEntity;
import org.riwi.eventcatalog.exception.IdNotFoundException;
import org.riwi.eventcatalog.repository.VenueRepository;
import org.riwi.eventcatalog.service.IVenueService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VenueServiceImpl implements IVenueService {

    private final VenueRepository venueRepository;

    @Override
    public VenueResponseDTO create(VenueRequestDTO request) {
        VenueEntity venueToSave = new VenueEntity();
        BeanUtils.copyProperties(request, venueToSave);
        VenueEntity savedVenue = venueRepository.save(venueToSave);
        return entityToResponse(savedVenue);
    }

    @Override
    public Page<VenueBasicResponseDTO> getAll(Pageable pageable) {
        return venueRepository.findAll(pageable).map(this::entityToBasicResponse);
    }

    @Override
    @Transactional
    public VenueResponseDTO findById(String id) {
        VenueEntity venue = venueRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("Venue"));
        return entityToResponse(venue);
    }

    @Override
    public VenueResponseDTO update(String id, VenueRequestDTO request) {
        VenueEntity venueToUpdate = venueRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("Venue"));
        BeanUtils.copyProperties(request, venueToUpdate);
        VenueEntity updatedVenue = venueRepository.save(venueToUpdate);
        return entityToResponse(updatedVenue);
    }

    @Override
    public void delete(String id) {
        venueRepository.deleteById(id);
    }

    private VenueResponseDTO entityToResponse(VenueEntity entity) {
        VenueResponseDTO response = new VenueResponseDTO();
        BeanUtils.copyProperties(entity, response);
        if (entity.getEvents() != null) {
            response.setEvents(entity.getEvents().stream().map(event -> {
                EventResponseDTO eventDTO = new EventResponseDTO();
                BeanUtils.copyProperties(event, eventDTO);
                // Evitar referencia circular en la respuesta
                eventDTO.setVenue(null);
                return eventDTO;
            }).collect(Collectors.toList()));
        }
        return response;
    }

    private VenueBasicResponseDTO entityToBasicResponse(VenueEntity entity) {
        VenueBasicResponseDTO response = new VenueBasicResponseDTO();
        BeanUtils.copyProperties(entity, response);
        return response;
    }
}
