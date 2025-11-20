package org.riwi.eventcatalog.service.impl;

import org.riwi.eventcatalog.dto.VenueDto;
import org.riwi.eventcatalog.exception.ResourceNotFoundException;
import org.riwi.eventcatalog.repository.VenueRepository;
import org.riwi.eventcatalog.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VenueServiceImpl implements IService<VenueDto> {
    private final VenueRepository venueRepository;

    @Autowired
    public VenueServiceImpl(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Override
    public VenueDto create(VenueDto venue) {
        return venueRepository.save(venue);
    }

    @Override
    public List<VenueDto> findAll() {
        return venueRepository.findAll();
    }

    @Override
    public VenueDto findById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with ID: " + id));
    }

    @Override
    public VenueDto update(Long id, VenueDto venue) {
        findById(id);
        venue.setId(id);
        return venueRepository.save(venue);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        venueRepository.delete(id);
    }
}
