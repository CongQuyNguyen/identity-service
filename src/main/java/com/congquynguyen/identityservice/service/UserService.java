package com.congquynguyen.identityservice.service;

import com.congquynguyen.identityservice.dto.request.UserCreationRequest;
import com.congquynguyen.identityservice.dto.request.UserUpdateRequest;
import com.congquynguyen.identityservice.dto.response.UserResponse;
import com.congquynguyen.identityservice.entity.UserEntity;
import com.congquynguyen.identityservice.enums.Role;
import com.congquynguyen.identityservice.exception.AppException;
import com.congquynguyen.identityservice.exception.ErrorCode;
import com.congquynguyen.identityservice.mapper.UserMapper;
import com.congquynguyen.identityservice.repository.RoleRepository;
import com.congquynguyen.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EnableMethodSecurity   // Bật authorize bằng annotation - method
public class UserService {

    UserRepository userRepository;

    UserMapper userMapper;

    PasswordEncoder passwordEncoder;

    RoleRepository roleRepository;

    public boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public UserResponse createRequest(UserCreationRequest request) {

        // Nếu có lỗi thì chỉ cần throw một AppException với một ErrorCode đã được define sẵn
        if(existByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);
        UserEntity userEntity = userMapper.toUserEntity(request);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

//        HashSet<String> roles = new HashSet<>();
//        roles.add(Role.USER.name());
//        userEntity.setRoles(roles);
        return userMapper.toUserResponse(userRepository.save(userEntity));
    }

    // Có thể thay thế bằng các cái endpoint ở file config, nhưng dùng cách này phổ biến hơn
    @PreAuthorize("hasRole('ADMIN')")   // Ngoài ra có thể hasAuthority để xác định permission
    public List<UserResponse> getAllUsers() {
        log.info("In method get all user");
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    @PostAuthorize("returnObject.username = authentication.name")
    public UserResponse getUserById(String id) {
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED)));
    }

    // Lấy ra thông tin của bản thân thông qua token
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        var myInfoEntity = userRepository.findByUsername(username).orElseThrow(()
                -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(myInfoEntity);
    }

    public UserResponse updateUser(String id, UserUpdateRequest request) {

        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUserEntity(userEntity, request);
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));

        // Set role
        var roles = roleRepository.findAllById(request.getRoles());
        userEntity.setRoles(new HashSet(roles));

        return userMapper.toUserResponse(userRepository.save(userEntity));
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
