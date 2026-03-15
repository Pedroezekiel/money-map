package com.projects.money_map_api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {


    private int status;

    private boolean isSuccessful;

    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;


    private T data;

    private static final String SUCCESSFUL = "Successful";

    public static Object ok(Object data) {
        return BaseResponse.builder()
                .data(data)
                .message(SUCCESSFUL)
                .status(200)
                .isSuccessful(true)
                .timestamp(LocalDateTime.now())
                .build();
    }


    public static BaseResponse<String> ok(String data) {
        return BaseResponse.<String>builder()
                .data(data)
                .message(SUCCESSFUL)
                .status(200)
                .isSuccessful(true)
                .timestamp(LocalDateTime.now())
                .build();
    }




    public static <T> BaseResponse<T> ok(int statusCode, String message, T data) {
        return BaseResponse.<T>builder()
                .data(data)
                .message(message)
                .status(statusCode)
                .isSuccessful(true)
                .timestamp(LocalDateTime.now())
                .build();
    }


    public static <T> BaseResponse<T> ok(int statusCode, String message) {
        return BaseResponse.<T>builder()
                .status(statusCode)
                .isSuccessful(false)
                .message(message)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }
}