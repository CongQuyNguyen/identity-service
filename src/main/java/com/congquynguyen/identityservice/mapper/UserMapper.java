package com.congquynguyen.identityservice.mapper;

import com.congquynguyen.identityservice.dto.request.UserCreationRequest;
import com.congquynguyen.identityservice.dto.request.UserUpdateRequest;
import com.congquynguyen.identityservice.dto.response.UserResponse;
import com.congquynguyen.identityservice.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Không map cái role ở đây, mà sẽ map ở service khi update hay create
    @Mapping(target = "roles", ignore = true)
    UserEntity toUserEntity(UserCreationRequest userCreationRequest);

    @Mapping(target = "roles", ignore = true)
    void updateUserEntity(@MappingTarget UserEntity userEntity,
                          UserUpdateRequest userUpdateRequest);

    UserResponse toUserResponse(UserEntity userEntity);
}
