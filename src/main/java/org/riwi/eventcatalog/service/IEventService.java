package org.riwi.eventcatalog.service;

import org.riwi.eventcatalog.dto.EventRequestDTO;
import org.riwi.eventcatalog.dto.EventResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface IEventService {
    EventResponseDTO create(EventRequestDTO request);
    Page<EventResponseDTO> getAll(Pageable pageable, String city, String category, LocalDate date);
    EventResponseDTO findById(String id);
    EventResponseDTO update(String id, EventRequestDTO request);
    void delete(String id);
}
