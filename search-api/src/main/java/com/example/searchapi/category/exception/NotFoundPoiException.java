package com.example.searchapi.category.exception;

import com.example.searchapi.common.CustomException;
import com.example.searchapi.common.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundPoiException extends CustomException {
    public NotFoundPoiException() {
        super(ErrorCode.NOT_FOUND_POI);
    }
}
