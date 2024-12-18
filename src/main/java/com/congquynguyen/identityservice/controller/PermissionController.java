package com.congquynguyen.identityservice.controller;

import com.congquynguyen.identityservice.dto.request.PermissionRequest;
import com.congquynguyen.identityservice.dto.response.ApiResponse;
import com.congquynguyen.identityservice.dto.response.PermissionResponse;
import com.congquynguyen.identityservice.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {

    @Autowired
    PermissionService permissionService;

    @PostMapping
    public ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest permissionRequest) {
        return ApiResponse.<PermissionResponse>builder()
                .code(200)
                .result(permissionService.createPermission(permissionRequest))
                .build();
    }

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAllPermissions() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .code(200)
                .result(permissionService.getAllPermissions())
                .build();
    }

    @DeleteMapping
    public void deletePermission(@PathVariable("permissionName") String permissionName) {
        permissionService.deletePermission(permissionName);
    }
}
