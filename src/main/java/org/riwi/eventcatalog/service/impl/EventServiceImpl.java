package org.riwi.eventcatalog.service.impl;

import org.riwi.eventcatalog.dto.EventDto;
import org.riwi.eventcatalog.exception.ResourceNotFoundException;
import org.riwi.eventcatalog.repository.EventRepository;
import org.riwi.eventcatalog.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements IService<EventDto> {

    private final EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public EventDto create(EventDto event) {
        return eventRepository.save(event);
    }

    @Override
    public List<EventDto> findAll() {
        return eventRepository.findAll();
    }

    @Override
    public EventDto findById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + id));
    }

    @Override
    public EventDto update(Long id, EventDto event) {
        findById(id);
        event.setId(id);
        return eventRepository.save(event);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        eventRepository.delete(id);
    }
}
