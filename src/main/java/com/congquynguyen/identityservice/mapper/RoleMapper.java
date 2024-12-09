package com.congquynguyen.identityservice.mapper;

import com.congquynguyen.identityservice.dto.request.RoleRequest;
import com.congquynguyen.identityservice.dto.response.RoleResponse;
import com.congquynguyen.identityservice.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    // Riêng cái permissions thì tự map, do request chỉ cung cấp một set tên, có
    // thể dựa vào tên đó để tìm kiếm
    @Mapping(target = "permissions", ignore = true)
    RoleEntity toRoleEntity(RoleRequest roleRequest);

    RoleResponse toRoleResponse(RoleEntity roleEntity);
}
