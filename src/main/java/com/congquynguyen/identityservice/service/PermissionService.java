package com.congquynguyen.identityservice.service;

import com.congquynguyen.identityservice.dto.request.PermissionRequest;
import com.congquynguyen.identityservice.dto.response.PermissionResponse;
import com.congquynguyen.identityservice.entity.PermissionEntity;
import com.congquynguyen.identityservice.mapper.PermissionMapper;
import com.congquynguyen.identityservice.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse createPermission(PermissionRequest permissionRequest) {
        PermissionEntity permission = permissionMapper.toPermissionEntity(permissionRequest);
        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    public List<PermissionResponse> getAllPermissions() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void deletePermission(String permissionName) {
        permissionRepository.deleteById(permissionName);
    }
}
