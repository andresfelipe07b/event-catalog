package org.riwi.eventcatalog.repository;

import java.util.List;
import java.util.Optional;

public interface IRepository<T> {
    T save(T entity);
    List<T> findAll();
    Optional<T> findById(Long id);
    void delete(Long id);
}
