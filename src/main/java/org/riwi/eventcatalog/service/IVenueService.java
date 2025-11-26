package org.riwi.eventcatalog.service;

import org.riwi.eventcatalog.dto.VenueBasicResponseDTO;
import org.riwi.eventcatalog.dto.VenueRequestDTO;
import org.riwi.eventcatalog.dto.VenueResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IVenueService {
    VenueResponseDTO create(VenueRequestDTO request);
    Page<VenueBasicResponseDTO> getAll(Pageable pageable);
    VenueResponseDTO findById(String id);
    VenueResponseDTO update(String id, VenueRequestDTO request);
    void delete(String id);
}
