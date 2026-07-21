package com.chamo.chamotech.exception;

import org.springframework.http.HttpStatus;

public class ResourceBadRequestException extends ApiException {

    public ResourceBadRequestException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

}
