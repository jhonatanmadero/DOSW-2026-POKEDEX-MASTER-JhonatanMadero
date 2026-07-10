package com.pokedex.persistence.mapper;

import com.pokedex.core.model.Role;
import com.pokedex.core.model.User;
import com.pokedex.persistence.entity.relational.RoleEntity;
import com.pokedex.persistence.entity.relational.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {

    User toDomain(UserEntity entity);

    default Role toRoleDomain(RoleEntity role) {
        return role == null ? null : Role.valueOf(role.name());
    }

    default RoleEntity toRoleEntity(Role role) {
        return role == null ? null : RoleEntity.valueOf(role.name());
    }
}
