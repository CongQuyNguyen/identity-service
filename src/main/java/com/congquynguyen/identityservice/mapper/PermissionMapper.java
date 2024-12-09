package com.congquynguyen.identityservice.mapper;

import com.congquynguyen.identityservice.dto.request.PermissionRequest;
import com.congquynguyen.identityservice.dto.response.PermissionResponse;
import com.congquynguyen.identityservice.entity.PermissionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionEntity toPermissionEntity(PermissionRequest permissionRequest);
    PermissionResponse toPermissionResponse(PermissionEntity permissionEntity);
}
