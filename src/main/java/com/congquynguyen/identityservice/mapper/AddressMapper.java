package com.congquynguyen.identityservice.mapper;

import com.congquynguyen.identityservice.dto.request.AddressRequest;
import com.congquynguyen.identityservice.dto.response.AddressResponse;
import com.congquynguyen.identityservice.entity.AddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    // Không map user, sẽ map ở service
    @Mapping(target = "user", ignore = true)
    AddressEntity toAddressEntity(AddressRequest addressRequest);

    AddressResponse toAddressResponse(AddressEntity addressEntity);
}
