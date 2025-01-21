package com.congquynguyen.identityservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AddressRequest {
    String country;
    String city;
    String district;
    String ward;
    String username;
}
