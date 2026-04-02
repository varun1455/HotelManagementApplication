package com.project.stayEase.advices;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiResponse<T> {

    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timeStamp;
    private boolean success;
    private T data;
    private ApiError error;

    public ApiResponse(boolean success, T data, ApiError error) {
        this.timeStamp = LocalDateTime.now();
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> successResponse(T data){
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> failureResponse(ApiError error){
        return new ApiResponse<>(false, null, error);
    }
}

