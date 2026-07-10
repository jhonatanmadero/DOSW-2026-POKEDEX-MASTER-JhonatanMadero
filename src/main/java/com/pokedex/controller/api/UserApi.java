package com.pokedex.controller.api;

import com.pokedex.controller.dto.request.ChangeRoleRequest;
import com.pokedex.controller.dto.request.UpdateUserRequest;
import com.pokedex.controller.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/** RF-21: perfil propio y administracion de usuarios por ADMIN. */
@Tag(name = "Users", description = "Perfil propio y administracion de usuarios")
@SecurityRequirement(name = "Bearer Authentication")
public interface UserApi {

    @Operation(summary = "Ver mi perfil")
    @GetMapping("/v1/users/me")
    ResponseEntity<UserResponse> me(Authentication authentication);

    @Operation(summary = "Actualizar mi perfil")
    @PutMapping("/v1/users/me")
    ResponseEntity<UserResponse> updateMe(Authentication authentication, @RequestBody UpdateUserRequest request);

    @Operation(summary = "Listar usuarios", description = "RF-21: solo ADMIN")
    @GetMapping("/v1/admin/users")
    ResponseEntity<List<UserResponse>> findAll();

    @Operation(summary = "Cambiar rol de usuario", description = "RF-21: solo ADMIN")
    @PatchMapping("/v1/admin/users/{id}/role")
    ResponseEntity<UserResponse> changeRole(@PathVariable Long id, @RequestBody ChangeRoleRequest request);

    @Operation(summary = "Desactivar usuario", description = "RF-21: solo ADMIN")
    @PatchMapping("/v1/admin/users/{id}/deactivate")
    ResponseEntity<Void> deactivate(@PathVariable Long id);

    @Operation(summary = "Eliminar usuario", description = "RF-21: solo ADMIN")
    @DeleteMapping("/v1/admin/users/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}
