package com.network.buddy.controller.advice;

import java.util.Arrays;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.network.buddy.model.ApiResponse;
import com.network.buddy.utils.ResponseUtil;
import com.network.buddy.utils.exception.ResourceNotFoundException;
import com.network.buddy.utils.exception.ResponseNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ApiResponse<Object> handleGeneralException(Exception ex, HttpServletRequest request) {
        return ResponseUtil.error(Arrays.asList(ex.getLocalizedMessage()), "An unexpected error occurred", 1001,
                request.getRequestURI());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiResponse<Object> handleResourceNotFoundException(ResourceNotFoundException ex,
            HttpServletRequest request) {
        return ResponseUtil.error(Arrays.asList(ex.getLocalizedMessage()), "Resource not found", 404,
                request.getRequestURI());
    }

    @ExceptionHandler(ResponseNotFoundException.class)
    public ApiResponse<Object> handleResponseNotFoundException(ResponseNotFoundException ex,
            HttpServletRequest request) {
        return ResponseUtil.error(Arrays.asList(ex.getLocalizedMessage()), "Response data not found", 204,
                request.getRequestURI());
    }

}
