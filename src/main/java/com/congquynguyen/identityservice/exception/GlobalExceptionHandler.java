package com.congquynguyen.identityservice.exception;

import com.congquynguyen.identityservice.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Lỗi này dùng khi không có một message nào xảy ra khi có lỗi
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<String>> exceptionHandler(Exception e) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<String>> appExceptionHandler(AppException e) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(e.getErrorCode().getCode());
        apiResponse.setMessage(e.getErrorCode().getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatusCode())   // Set HttpStatusCode
                .body(apiResponse);
    }

    // Exception xử lý quyền không đủ quyền để truy cập vào một endpoint (AccessDenied)
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse<String>> accessDeniedExceptionHandler(AccessDeniedException e) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(apiResponse);
    }

    // Đây là loại Exception khi sai yêu cầu về các field (Json)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<String>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        String enumKey = Objects.requireNonNull(e.getFieldError()).getDefaultMessage(); // Lấy ra constant đã khai báo bên field - chỗ message

        // Xử lý khi bị sai key validation
        ErrorCode errorCode = ErrorCode.VALIDATION_INVALID;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException ignored) {

        }

        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
