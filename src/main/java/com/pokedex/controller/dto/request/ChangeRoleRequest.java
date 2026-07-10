package com.pokedex.controller.dto.request;

import com.pokedex.core.model.Role;
import jakarta.validation.constraints.NotNull;

public record ChangeRoleRequest(@NotNull Role role) {}
