package com.example.searchapi.address.exception;

import com.example.searchapi.common.CustomException;
import com.example.searchapi.common.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundAddressException extends CustomException {
    public NotFoundAddressException() {
        super(ErrorCode.NOT_FOUND_ADDRESS);
    }
}
