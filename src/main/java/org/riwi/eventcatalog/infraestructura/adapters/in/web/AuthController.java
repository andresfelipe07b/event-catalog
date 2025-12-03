package org.riwi.eventcatalog.infraestructura.adapters.in.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.riwi.eventcatalog.dominio.model.User;
import org.riwi.eventcatalog.dominio.ports.in.UserUseCase;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.AuthResponse;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.LoginRequest;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.RegisterRequest;
import org.riwi.eventcatalog.infraestructura.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final UserUseCase userUseCase;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());

        User registeredUser = userUseCase.registerUser(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(registeredUser.getUsername());
        String jwtToken = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(jwtToken));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String jwtToken = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(jwtToken));
    }
}
