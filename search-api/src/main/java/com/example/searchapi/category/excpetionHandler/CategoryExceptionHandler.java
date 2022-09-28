package com.example.searchapi.category.excpetionHandler;

import com.example.searchapi.common.ErrorResponse;
import com.example.searchapi.category.exception.NotFoundPoiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CategoryExceptionHandler {

    @ExceptionHandler(NotFoundPoiException.class)
    protected ResponseEntity<?> notFoundPoiExceptionHandler(NotFoundPoiException e){
        log.error("not found poi entity", e);
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }
}
