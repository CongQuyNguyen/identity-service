package com.congquynguyen.identityservice.controller;

import com.congquynguyen.identityservice.dto.request.UserCreationRequest;
import com.congquynguyen.identityservice.dto.request.UserUpdateRequest;
import com.congquynguyen.identityservice.dto.response.ApiResponse;
import com.congquynguyen.identityservice.entity.UserEntity;
import com.congquynguyen.identityservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping()
    ApiResponse<UserEntity> createUser(@RequestBody @Valid UserCreationRequest userCreationRequest) {
        ApiResponse<UserEntity> apiResponse = new ApiResponse<>();

        apiResponse.setCode(200);
        apiResponse.setResult(userService.createRequest(userCreationRequest));

        return apiResponse;
    }

    @GetMapping()
    List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    UserEntity getUserById(@PathVariable("userId") String userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/{userId}")
    UserEntity updateUser(@PathVariable("userId") String userId,
                          @RequestBody @Valid UserUpdateRequest userUpdateRequest) {
        return userService.updateUser(userId, userUpdateRequest);
    }

    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
    }
}
