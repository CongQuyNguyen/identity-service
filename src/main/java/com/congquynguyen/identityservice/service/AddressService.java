package com.congquynguyen.identityservice.service;

import com.congquynguyen.identityservice.dto.request.AddressRequest;
import com.congquynguyen.identityservice.dto.response.AddressResponse;
import com.congquynguyen.identityservice.entity.AddressEntity;
import com.congquynguyen.identityservice.entity.UserEntity;
import com.congquynguyen.identityservice.exception.AppException;
import com.congquynguyen.identityservice.exception.ErrorCode;
import com.congquynguyen.identityservice.mapper.AddressMapper;
import com.congquynguyen.identityservice.repository.AddressRepository;
import com.congquynguyen.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressService {

    AddressRepository addressRepository;
    AddressMapper addressMapper;
    UserRepository userRepository;


    public AddressResponse createAddress(AddressRequest addressRequest) {

        UserEntity userEntity = userRepository.findByUsername(addressRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        AddressEntity addressEntity = addressMapper.toAddressEntity(addressRequest);

        // Gán liên kết
        addressEntity.setUser(userEntity);
        userEntity.getAddresses().add(addressEntity);

        // Lưu
        try {
            var address = addressRepository.save(addressEntity);
            return addressMapper.toAddressResponse(address);
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }
}
