package com.congquynguyen.identityservice.controller;

import com.congquynguyen.identityservice.dto.request.AddressRequest;
import com.congquynguyen.identityservice.dto.response.AddressResponse;
import com.congquynguyen.identityservice.dto.response.ApiResponse;
import com.congquynguyen.identityservice.service.AddressService;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressController {

    AddressService addressService;

    @PostMapping
    ApiResponse<AddressResponse> createAddress(@RequestBody @Valid AddressRequest addressRequest) {
        log.info("Create address request: {}", addressRequest);
        return ApiResponse.<AddressResponse>builder()
                .code(200)
                .result(addressService.createAddress(addressRequest))
                .build();
    }
}
