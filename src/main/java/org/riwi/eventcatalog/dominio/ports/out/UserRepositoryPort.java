package org.riwi.eventcatalog.dominio.ports.out;

import org.riwi.eventcatalog.dominio.model.User;

import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findByUsername(String username);
}
