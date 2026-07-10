package com.pokedex.core.service.impl;

import com.pokedex.core.exception.DuplicateResourceException;
import com.pokedex.core.exception.InvalidCredentialsException;
import com.pokedex.core.model.Role;
import com.pokedex.core.model.User;
import com.pokedex.core.port.UserPersistencePort;
import com.pokedex.core.service.interfaces.AuthService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementa RF-01 (registro/login) y RF-02 (login con Gmail).
 * La generacion del token JWT se delega a un TokenIssuer para no acoplar
 * el core a la capa de seguridad (Seccion 3.2: core no depende de security).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserPersistencePort userPort;
    private final PasswordEncoder passwordEncoder;
    private final TokenIssuer tokenIssuer;

    @Override
    @Transactional
    public AuthResult register(User user, String rawPassword) {
        if (userPort.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("Usuario", "email", user.getEmail());
        }
        User toSave = user.toBuilder()
                .passwordHash(passwordEncoder.encode(rawPassword))
                .role(Role.TRAINER)
                .active(true)
                .fromGoogle(false)
                .createdAt(LocalDateTime.now())
                .build();
        User saved = userPort.save(toSave);
        log.info("Usuario registrado: {}", saved.getEmail());
        return new AuthResult(saved, tokenIssuer.issue(saved));
    }

    @Override
    public AuthResult login(String email, String rawPassword) {
        User user = userPort.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);
        if (user.getPasswordHash() == null
                || !passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }
        return new AuthResult(user, tokenIssuer.issue(user));
    }

    @Override
    @Transactional
    public AuthResult loginOrRegisterWithGoogle(String email, String fullName, String avatarUrl) {
        User user = userPort.findByEmail(email).orElseGet(() -> {
            User newUser = User.builder()
                    .fullName(fullName)
                    .email(email)
                    .avatarUrl(avatarUrl)
                    .role(Role.TRAINER)
                    .active(true)
                    .fromGoogle(true)
                    .createdAt(LocalDateTime.now())
                    .build();
            log.info("Creando usuario automaticamente via Google OAuth2: {}", email);
            return userPort.save(newUser);
        });
        return new AuthResult(user, tokenIssuer.issue(user));
    }

    /** Puerto minimo hacia la capa security para no invertir la dependencia core->security. */
    public interface TokenIssuer {
        String issue(User user);
    }
}
