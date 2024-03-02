package com.flap.app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response<T> {


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime timestamp = LocalDateTime.now();
    private String message = "";
    private boolean success = true;
    private Integer status = 200;
    private T data;

    public Response(String message) {
        this.message = message;
    }

    public Response(String message, Integer status) {
        this.message = message;
        this.status = status;
    }

    public Response(String message, Integer status, boolean success) {
        this.message = message;
        this.status = status;
        this.success = success;
    }

    public Response(String message, Integer status, T data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public Response(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public Response(T data) {
        this.data = data;
    }

    public Response(T data, String message, boolean success, Integer status) {
        this.data = data;
        this.message = message;
        this.success = success;
        this.status = status;
    }


    public Response(String message, int status, boolean success) {
        this.message = message;
        this.success = success;
        this.status = status;
    }
}
