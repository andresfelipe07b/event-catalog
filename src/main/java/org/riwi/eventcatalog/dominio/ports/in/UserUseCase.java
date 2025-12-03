package org.riwi.eventcatalog.dominio.ports.in;

import org.riwi.eventcatalog.dominio.model.User;

public interface UserUseCase {
    User registerUser(User user);
}
