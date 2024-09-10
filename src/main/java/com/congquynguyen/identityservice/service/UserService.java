package com.congquynguyen.identityservice.service;

import com.congquynguyen.identityservice.dto.request.UserCreationRequest;
import com.congquynguyen.identityservice.dto.request.UserUpdateRequest;
import com.congquynguyen.identityservice.entity.UserEntity;
import com.congquynguyen.identityservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;

    public UserEntity createRequest(UserCreationRequest request) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(request.getUsername());
        userEntity.setPassword(request.getPassword());
        userEntity.setEmail(request.getEmail());
        userEntity.setFirstName(request.getFirstName());
        userEntity.setLastName(request.getLastName());
        userEntity.setDob(request.getDob());

        return userRepository.save(userEntity);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserEntity updateUser(String id, UserUpdateRequest request) {
        UserEntity userEntity = getUserById(id);
        userEntity.setFirstName(request.getFirstName());
        userEntity.setLastName(request.getLastName());
        userEntity.setDob(request.getDob());
        userEntity.setPassword(request.getPassword());

        return userRepository.save(userEntity);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
