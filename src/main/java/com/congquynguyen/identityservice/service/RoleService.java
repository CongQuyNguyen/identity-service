package com.congquynguyen.identityservice.service;

import com.congquynguyen.identityservice.dto.request.RoleRequest;
import com.congquynguyen.identityservice.dto.response.RoleResponse;
import com.congquynguyen.identityservice.exception.AppException;
import com.congquynguyen.identityservice.exception.ErrorCode;
import com.congquynguyen.identityservice.mapper.RoleMapper;
import com.congquynguyen.identityservice.repository.PermissionRepository;
import com.congquynguyen.identityservice.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {

    RoleRepository roleRepository;

    PermissionRepository permissionRepository;

    private final RoleMapper roleMapper;


    public RoleResponse createRole(RoleRequest roleRequest) {
        var roleEntity = roleMapper.toRoleEntity(roleRequest);

        // Tiến hành thủ công permissions
        var permissions = permissionRepository.findAllById(roleRequest.getPermissions());
        roleEntity.setPermissions(new HashSet<>(permissions));

        return roleMapper.toRoleResponse(roleRepository.save(roleEntity));
    }

    public List<RoleResponse> getAllRoles() {
        var roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toRoleResponse).toList();
    }
}
