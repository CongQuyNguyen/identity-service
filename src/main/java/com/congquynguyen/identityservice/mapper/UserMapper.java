package com.congquynguyen.identityservice.mapper;

import com.congquynguyen.identityservice.dto.request.UserCreationRequest;
import com.congquynguyen.identityservice.dto.request.UserUpdateRequest;
import com.congquynguyen.identityservice.dto.response.UserResponse;
import com.congquynguyen.identityservice.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toUserEntity(UserResponse userResponse);
    UserEntity toUserEntity(UserCreationRequest userCreationRequest);
    void updateUserEntity(@MappingTarget UserResponse userResponse,
                          UserUpdateRequest userUpdateRequest);
    UserResponse toUserResponse(UserEntity userEntity);
}
