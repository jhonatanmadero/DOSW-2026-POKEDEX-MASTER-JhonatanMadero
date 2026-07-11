package com.pokedex.controller.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.pokedex.controller.dto.response.ApiError;
import com.pokedex.core.exception.BusinessException;
import com.pokedex.core.exception.DuplicateResourceException;
import com.pokedex.core.exception.ForbiddenOperationException;
import com.pokedex.core.exception.InvalidCredentialsException;
import com.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.core.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock private HttpServletRequest request;

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @BeforeEach
    void setUp() {
        when(request.getRequestURI()).thenReturn("/api/v1/pokemon/999");
    }

    @Test
    @DisplayName("RF-06: 404 cuando el recurso no existe")
    void handleNotFound_returns404() {
        ResponseEntity<ApiError> response = handler.handleNotFound(
                new ResourceNotFoundException("Pokemon", "id", 999L), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().errorCode()).isEqualTo("NOT_FOUND");
        assertThat(response.getBody().path()).isEqualTo("/api/v1/pokemon/999");
    }

    @Test
    @DisplayName("RN-04: 409 cuando el recurso ya existe")
    void handleDuplicate_returns409() {
        ResponseEntity<ApiError> response = handler.handleDuplicate(
                new DuplicateResourceException("Pokemon", "numero", 25), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("RN-03: 400 cuando se viola una regla de negocio (ej. equipo > 6)")
    void handleValidation_returns400() {
        ResponseEntity<ApiError> response = handler.handleValidation(new ValidationException("max 6 pokemon"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().errorCode()).isEqualTo("VALIDATION_ERROR");
    }

    @Test
    @DisplayName("RF-01: 401 con credenciales invalidas")
    void handleInvalidCredentials_returns401() {
        ResponseEntity<ApiError> response = handler.handleInvalidCredentials(new InvalidCredentialsException(), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("RN-02: 403 cuando el usuario opera sobre recursos que no le pertenecen")
    void handleForbidden_returns403() {
        ResponseEntity<ApiError> response = handler.handleForbidden(
                new ForbiddenOperationException("No es tu equipo"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("403 cuando Spring Security deniega el acceso")
    void handleAccessDenied_returns403() {
        ResponseEntity<ApiError> response = handler.handleAccessDenied(new AccessDeniedException("denied"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody().errorCode()).isEqualTo("ACCESS_DENIED");
    }

    @Test
    @DisplayName("400 para excepciones de negocio genericas")
    void handleBusiness_returns400() {
        ResponseEntity<ApiError> response = handler.handleBusiness(new BusinessException("algo fallo", "GENERIC"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("500 para errores no controlados")
    void handleGeneric_returns500() {
        ResponseEntity<ApiError> response = handler.handleGeneric(new RuntimeException("boom"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().errorCode()).isEqualTo("INTERNAL_ERROR");
    }

    @Test
    @DisplayName("400 con detalle de campos cuando falla @Valid en el body")
    void handleMethodArgumentNotValid_returns400WithFieldErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("pokemonRequest", "name", "no puede estar vacio");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<ApiError> response = handler.handleValidation(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().fieldErrors()).hasSize(1);
        assertThat(response.getBody().fieldErrors().get(0).field()).isEqualTo("name");
    }
}
