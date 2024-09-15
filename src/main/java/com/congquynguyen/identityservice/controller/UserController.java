package com.congquynguyen.identityservice.controller;

import com.congquynguyen.identityservice.dto.request.UserCreationRequest;
import com.congquynguyen.identityservice.dto.request.UserUpdateRequest;
import com.congquynguyen.identityservice.dto.response.ApiResponse;
import com.congquynguyen.identityservice.dto.response.UserResponse;
import com.congquynguyen.identityservice.entity.UserEntity;
import com.congquynguyen.identityservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @PostMapping()
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest userCreationRequest) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .result(userService.createRequest(userCreationRequest))
                .build();
    }

    @GetMapping()
    ApiResponse<List<UserResponse>> getAllUsers() {
        checkInfo();

        return ApiResponse.<List<UserResponse>>builder()
                .code(200)
                .result(userService.getAllUsers())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUserById(@PathVariable("userId") String userId) {

        checkInfo();
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .result(userService.getUserById(userId))
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable("userId") String userId,
                          @RequestBody @Valid UserUpdateRequest userUpdateRequest) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .result(userService.updateUser(userId, userUpdateRequest))
                .build();
    }

    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
    }

    // Demo to see username and scope
    private void checkInfo() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> {
            log.info("GrantedAuthority: {}", grantedAuthority);
        });
    }
}
