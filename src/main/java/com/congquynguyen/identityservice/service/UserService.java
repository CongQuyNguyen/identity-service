package com.congquynguyen.identityservice.service;

import com.congquynguyen.identityservice.dto.request.UserCreationRequest;
import com.congquynguyen.identityservice.dto.request.UserUpdateRequest;
import com.congquynguyen.identityservice.entity.UserEntity;
import com.congquynguyen.identityservice.exception.AppException;
import com.congquynguyen.identityservice.exception.ErrorCode;
import com.congquynguyen.identityservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;

    public boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public UserEntity createRequest(UserCreationRequest request) {

        // Nếu có lỗi thì chỉ cần throw một AppException với một ErrorCode đã được define sẵn
        if(existByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

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
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED));
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
