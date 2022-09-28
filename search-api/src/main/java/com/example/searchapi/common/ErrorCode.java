package com.example.searchapi.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    NOT_FOUND_ADDRESS(NOT_FOUND, "ADDRESS를 찾을 수 없습니다."),
    NOT_FOUND_POI(NOT_FOUND, "POI를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
