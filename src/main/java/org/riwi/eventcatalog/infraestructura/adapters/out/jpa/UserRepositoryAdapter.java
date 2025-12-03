package org.riwi.eventcatalog.infraestructura.adapters.out.jpa;

import lombok.AllArgsConstructor;
import org.riwi.eventcatalog.dominio.model.User;
import org.riwi.eventcatalog.dominio.ports.out.UserRepositoryPort;
import org.riwi.eventcatalog.infraestructura.adapters.out.jpa.entity.UserJpaEntity;
import org.riwi.eventcatalog.infraestructura.adapters.out.jpa.mapper.UserJpaMapper;
import org.riwi.eventcatalog.infraestructura.adapters.out.jpa.repository.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;
    private final UserJpaMapper userJpaMapper;

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID().toString());
        }
        UserJpaEntity userJpaEntity = userJpaMapper.toEntity(user);
        UserJpaEntity savedEntity = userJpaRepository.save(userJpaEntity);
        return userJpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username)
                .map(userJpaMapper::toDomain);
    }
}
