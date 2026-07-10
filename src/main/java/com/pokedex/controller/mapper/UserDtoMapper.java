package com.pokedex.controller.mapper;

import com.pokedex.controller.dto.response.TokenResponse;
import com.pokedex.controller.dto.response.UserResponse;
import com.pokedex.core.model.User;
import com.pokedex.core.service.interfaces.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    @Mapping(source = "role", target = "role")
    UserResponse toResponse(User user);

    default String map(com.pokedex.core.model.Role role) {
        return role == null ? null : role.name();
    }

    default TokenResponse toTokenResponse(AuthService.AuthResult result) {
        return new TokenResponse(result.token(), toResponse(result.user()));
    }
}
