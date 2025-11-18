package org.riwi.eventcatalog.service;

import org.riwi.eventcatalog.dto.EventDto;

import java.util.List;

public interface EventService {
    EventDto create(EventDto eventDto);
    List<EventDto> findAll();
    EventDto findById(Long id);
    EventDto update(Long id, EventDto event);
    void delete(Long id);
}
