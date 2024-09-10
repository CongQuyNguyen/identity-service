package com.congquynguyen.identityservice.controller;

import com.congquynguyen.identityservice.dto.request.UserCreationRequest;
import com.congquynguyen.identityservice.dto.request.UserUpdateRequest;
import com.congquynguyen.identityservice.entity.UserEntity;
import com.congquynguyen.identityservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping()
    UserEntity createUser(@RequestBody UserCreationRequest userCreationRequest) {
        return userService.createRequest(userCreationRequest);
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
                          @RequestBody UserUpdateRequest userUpdateRequest) {
        return userService.updateUser(userId, userUpdateRequest);
    }

    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
    }
}
