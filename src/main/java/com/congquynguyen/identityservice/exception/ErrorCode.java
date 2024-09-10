package com.congquynguyen.identityservice.exception;

public enum ErrorCode {
    USER_EXISTED(1000, "User already exists"),
    UNCATEGORIZED(9999, "Uncategorized"),
    USERNAME_INVALID(1001, "This field must be least 3 characters"),
    PASSWORD_INVALID(1002, "Password must be at least 8 characters"),
    VALIDATION_INVALID(1111, "Key validation failed"),
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private final int code;
    private final String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
