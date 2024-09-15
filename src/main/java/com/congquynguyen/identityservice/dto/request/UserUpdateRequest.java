package com.congquynguyen.identityservice.dto.request;

import com.congquynguyen.identityservice.validation.MinAge;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserUpdateRequest {

    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;

    @NotEmpty(message = "First name is not empty")
    String firstName;

    @NotEmpty(message = "Last name is not empty")
    String lastName;

    @MinAge(value = 16, message = "You must be at least 16 years old to register")
    LocalDate dob;

    Set<String> roles;
}
