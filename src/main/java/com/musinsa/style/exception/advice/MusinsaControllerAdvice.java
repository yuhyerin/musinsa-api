package com.musinsa.style.exception.advice;

import com.musinsa.style.controller.common.MusinsaResponse;
import com.musinsa.style.enums.ResponseCode;
import com.musinsa.style.exception.BrandDeleteConflictException;
import com.musinsa.style.exception.BrandNotFoundException;
import com.musinsa.style.exception.CategoryNotFoundException;
import com.musinsa.style.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class MusinsaControllerAdvice {

    // @Valid 검증 실패 시 발생하는 MethodArgumentNotValidException 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MusinsaResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

        ObjectError error = ex.getBindingResult().getAllErrors().get(0);

        MusinsaResponse response = MusinsaResponse.builder()
                .result(ResponseCode.BAD_REQUEST.result)
                .message(error.getDefaultMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    protected ResponseEntity<MusinsaResponse> handleCategoryNotFoundException(CategoryNotFoundException categoryNotFoundException) {
        MusinsaResponse response = MusinsaResponse.builder()
                .result(categoryNotFoundException.getResult())
                .message(categoryNotFoundException.getMessage())
                .build();

        return ResponseEntity.status(categoryNotFoundException.getStatus())
                .body(response);
    }

    @ExceptionHandler(BrandNotFoundException.class)
    protected ResponseEntity<MusinsaResponse> handleBrandNotFoundException(BrandNotFoundException brandNotFoundException) {
        MusinsaResponse response = MusinsaResponse.builder()
                .result(brandNotFoundException.getResult())
                .message(brandNotFoundException.getMessage())
                .build();

        return ResponseEntity.status(brandNotFoundException.getStatus())
                .body(response);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    protected ResponseEntity<MusinsaResponse> handleProductNotFoundException(ProductNotFoundException productNotFoundException) {
        MusinsaResponse response = MusinsaResponse.builder()
                .result(productNotFoundException.getResult())
                .message(productNotFoundException.getMessage())
                .build();

        return ResponseEntity.status(productNotFoundException.getStatus())
                .body(response);
    }

    @ExceptionHandler(BrandDeleteConflictException.class)
    protected ResponseEntity<MusinsaResponse> handleBrandDeleteConflictException(BrandDeleteConflictException brandDeleteConflictException) {
        MusinsaResponse response = MusinsaResponse.builder()
                .result(brandDeleteConflictException.getResult())
                .message(brandDeleteConflictException.getMessage())
                .build();

        return ResponseEntity.status(brandDeleteConflictException.getStatus())
                .body(response);
    }


    @ExceptionHandler(Throwable.class)
    protected ResponseEntity<MusinsaResponse> handleThrowable(Throwable throwable) {
        MusinsaResponse response = MusinsaResponse.builder()
                .result(ResponseCode.INTERNAL_SERVER_ERROR.result)
                .message(ResponseCode.INTERNAL_SERVER_ERROR.message)
                .build();

        log.error("Server error: {}", throwable.getMessage(), throwable);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);

    }

}
