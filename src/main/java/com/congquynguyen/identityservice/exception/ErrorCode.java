package com.congquynguyen.identityservice.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    USER_NOT_EXISTED(1000, "User is not exists", HttpStatus.NOT_FOUND),
    USER_EXISTED(1000, "User already exists", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED(9999, "Uncategorized", HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_INVALID(1001, "This field must be least 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1002, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    VALIDATION_INVALID(1111, "Key validation failed", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1003, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1004, "You do not have permission", HttpStatus.FORBIDDEN),
    ;

    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    int code;
    String message;
    HttpStatusCode httpStatusCode;
}
