package org.riwi.eventcatalog.aplicacion.usecase;

import lombok.AllArgsConstructor;
import org.riwi.eventcatalog.dominio.model.User;
import org.riwi.eventcatalog.dominio.ports.in.UserUseCase;
import org.riwi.eventcatalog.dominio.ports.out.UserRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user) {
        userRepositoryPort.findByUsername(user.getUsername()).ifPresent(u -> {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        });
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepositoryPort.save(user);
    }
}
