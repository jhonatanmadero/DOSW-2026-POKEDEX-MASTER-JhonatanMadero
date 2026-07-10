package com.pokedex.controller.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

/** RF-01: registro de usuario. */
public record RegisterRequest(
        @NotBlank(message = "El nombre completo es obligatorio")
        @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
        String fullName,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "Formato de correo invalido")
        String email,

        @NotBlank(message = "La contrasena es obligatoria")
        @Size(min = 8, message = "La contrasena debe tener minimo 8 caracteres")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9]).+$",
                message = "La contrasena debe tener al menos una mayuscula y un numero")
        String password,

        @NotBlank(message = "Debes confirmar la contrasena")
        String confirmPassword,

        LocalDate birthDate,

        String favoriteRegion
) {}
