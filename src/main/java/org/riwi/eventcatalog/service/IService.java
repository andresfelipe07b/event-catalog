package org.riwi.eventcatalog.service;

import org.riwi.eventcatalog.dto.EventDto;

import java.util.List;

public interface IService<T> {
    T create(T dto);
    List<T> findAll();
    T findById(Long id);
    T update(Long id, T dto);
    void delete(Long id);
}
