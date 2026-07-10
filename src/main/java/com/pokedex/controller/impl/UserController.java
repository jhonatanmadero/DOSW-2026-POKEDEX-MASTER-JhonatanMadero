package com.pokedex.controller.impl;

import com.pokedex.controller.api.UserApi;
import com.pokedex.controller.dto.request.ChangeRoleRequest;
import com.pokedex.controller.dto.request.UpdateUserRequest;
import com.pokedex.controller.dto.response.UserResponse;
import com.pokedex.controller.mapper.UserDtoMapper;
import com.pokedex.core.model.User;
import com.pokedex.core.service.interfaces.UserService;
import com.pokedex.security.CustomUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;
    private final UserDtoMapper mapper;

    private Long currentUserId(Authentication authentication) {
        return ((CustomUserDetails) authentication.getPrincipal()).getUserId();
    }

    @Override
    public ResponseEntity<UserResponse> me(Authentication authentication) {
        return ResponseEntity.ok(mapper.toResponse(userService.findById(currentUserId(authentication))));
    }

    @Override
    public ResponseEntity<UserResponse> updateMe(Authentication authentication, UpdateUserRequest request) {
        User changes = User.builder()
                .fullName(request.fullName())
                .favoriteRegion(request.favoriteRegion())
                .birthDate(request.birthDate())
                .build();
        return ResponseEntity.ok(mapper.toResponse(userService.updateProfile(currentUserId(authentication), changes)));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll().stream().map(mapper::toResponse).toList());
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> changeRole(Long id, ChangeRoleRequest request) {
        return ResponseEntity.ok(mapper.toResponse(userService.changeRole(id, request.role())));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivate(Long id) {
        userService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
