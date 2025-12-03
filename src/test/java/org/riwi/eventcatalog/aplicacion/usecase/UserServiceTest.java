package org.riwi.eventcatalog.aplicacion.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.riwi.eventcatalog.dominio.model.Role;
import org.riwi.eventcatalog.dominio.model.User;
import org.riwi.eventcatalog.dominio.ports.out.UserRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_shouldRegisterNewUserSuccessfully() {
        // Given
        User userToRegister = new User();
        userToRegister.setUsername("testuser");
        userToRegister.setPassword("rawpassword");
        userToRegister.setRole(Role.USER);

        when(userRepositoryPort.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("rawpassword")).thenReturn("encodedpassword");
        when(userRepositoryPort.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId("generated-id");
            return savedUser;
        });

        // When
        User registeredUser = userService.registerUser(userToRegister);

        // Then
        assertNotNull(registeredUser.getId());
        assertEquals("testuser", registeredUser.getUsername());
        assertEquals("encodedpassword", registeredUser.getPassword());
        assertEquals(Role.USER, registeredUser.getRole());
        verify(userRepositoryPort, times(1)).findByUsername("testuser");
        verify(passwordEncoder, times(1)).encode("rawpassword");
        verify(userRepositoryPort, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_shouldThrowExceptionIfUsernameAlreadyExists() {
        // Given
        User existingUser = new User();
        existingUser.setUsername("existinguser");
        existingUser.setPassword("anypassword");

        when(userRepositoryPort.findByUsername("existinguser")).thenReturn(Optional.of(existingUser));

        // When & Then
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(existingUser);
        });

        assertEquals("El nombre de usuario ya está en uso.", thrown.getMessage());
        verify(userRepositoryPort, times(1)).findByUsername("existinguser");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepositoryPort, never()).save(any(User.class));
    }
}
