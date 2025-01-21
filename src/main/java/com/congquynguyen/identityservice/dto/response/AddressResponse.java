package com.congquynguyen.identityservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AddressResponse {
    String country;
    String city;
    String ward;
    UserResponse user;
}
