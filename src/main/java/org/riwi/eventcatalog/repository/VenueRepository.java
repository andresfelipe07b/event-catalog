package org.riwi.eventcatalog.repository;

import org.riwi.eventcatalog.dto.VenueDto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class VenueRepository implements IRepository<VenueDto> {
    private final List<VenueDto> venues = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public VenueDto save(VenueDto venue) {
        if (venue.getId() == null) {
            venue.setId(idCounter.getAndIncrement());
            venues.add(venue);
        } else {
            delete(venue.getId());
            venues.add(venue);
        }
        return venue;
    }

    @Override
    public List<VenueDto> findAll() {
        return new ArrayList<>(venues);
    }

    @Override
    public Optional<VenueDto> findById(Long id) {
        return venues.stream().filter(v -> v.getId().equals(id)).findFirst();
    }

    @Override
    public void delete(Long id) {
        venues.removeIf(v -> v.getId().equals(id));
    }

}
