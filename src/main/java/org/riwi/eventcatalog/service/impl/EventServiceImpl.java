package org.riwi.eventcatalog.service.impl;

import org.riwi.eventcatalog.dto.EventDto;
import org.riwi.eventcatalog.repository.EventRepository;
import org.riwi.eventcatalog.service.IService;

import java.util.List;

public class EventServiceImpl implements IService<EventDto> {

    private final EventRepository eventRepository;

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
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    public EventDto update(Long id, EventDto event) {
        EventDto existingEvent = findById(id);
        if (existingEvent != null) {
            event.setId(id);
            return eventRepository.save(event);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        eventRepository.delete(id);

    }
}
