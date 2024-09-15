package com.congquynguyen.identityservice.service;

import com.congquynguyen.identityservice.dto.request.UserCreationRequest;
import com.congquynguyen.identityservice.dto.request.UserUpdateRequest;
import com.congquynguyen.identityservice.dto.response.UserResponse;
import com.congquynguyen.identityservice.entity.UserEntity;
import com.congquynguyen.identityservice.enums.Role;
import com.congquynguyen.identityservice.exception.AppException;
import com.congquynguyen.identityservice.exception.ErrorCode;
import com.congquynguyen.identityservice.mapper.UserMapper;
import com.congquynguyen.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

// Hai dòng kia thay cho autowire và private final
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;

    UserMapper userMapper;

    PasswordEncoder passwordEncoder;

    public boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public UserResponse createRequest(UserCreationRequest request) {

        // Nếu có lỗi thì chỉ cần throw một AppException với một ErrorCode đã được define sẵn
        if(existByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);
        UserEntity userEntity = userMapper.toUserEntity(request);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        userEntity.setRoles(roles);
        return userMapper.toUserResponse(userRepository.save(userEntity));
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(String id) {
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED)));
    }

    public UserResponse updateUser(String id, UserUpdateRequest request) {

        UserResponse userResponse = getUserById(id);

        userMapper.updateUserEntity(userResponse, request);

        return userMapper.toUserResponse(userMapper.toUserEntity(userResponse));
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
