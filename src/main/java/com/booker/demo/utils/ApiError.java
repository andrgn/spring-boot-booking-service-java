package com.booker.demo.utils;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ApiError(HttpStatus status, List<String> errorMessages) {
    public ApiError(HttpStatus status, String errorMessage) {
        this(status, List.of(errorMessage));
    }
}
