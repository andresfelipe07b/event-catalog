package org.riwi.eventcatalog.repository;

import org.riwi.eventcatalog.dto.EventDto;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class EventRepository implements IRepository<EventDto>{
    private final List<EventDto> events = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public EventDto save(EventDto event) {
        if (event.getId() == null) {
            event.setId(idCounter.getAndIncrement());
            events.add(event);
        } else {
            delete(event.getId());
            events.add(event);
        }
        return event;
    }

    @Override
    public List<EventDto> findAll() {
        return new ArrayList<>(events);
    }

    @Override
    public Optional<EventDto> findById(Long id) {
        return events.stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    @Override
    public void delete(Long id) {
        events.removeIf(e -> e.getId().equals(id));
    }
}
