package com.example.searchapi.address.exception;

import com.example.searchapi.common.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class AddressExceptionHandler {

    @ExceptionHandler(NotFoundAddressException.class)
    protected ResponseEntity<?> notFoundAddressExceptionHandler(NotFoundAddressException e) {
        log.error("not found address entity", e);
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }
}
