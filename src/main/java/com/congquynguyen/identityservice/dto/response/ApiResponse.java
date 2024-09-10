package com.congquynguyen.identityservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

// Cho biết khi response trả về thì field nào bị null sẽ không cần đưa ra
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private int code;
    private String message;
    private T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
