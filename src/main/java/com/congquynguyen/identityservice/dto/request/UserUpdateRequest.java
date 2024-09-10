package com.congquynguyen.identityservice.dto.request;

import com.congquynguyen.identityservice.validation.MinAge;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class UserUpdateRequest {

    @Size(min = 8, message = "PASSWORD_INVALID")
    private String password;

    @NotEmpty(message = "First name is not empty")
    private String firstName;

    @NotEmpty(message = "Last name is not empty")
    private String lastName;

    @MinAge(value = 16, message = "You must be at least 16 years old to register")
    private LocalDate dob;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
}
