package com.simbrella.dev.loan_mgt_service.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {
    private String message;
    private String httpStatus;

    public ResourceNotFoundException(String message, String httpStatus) {
        super(message);
        this.httpStatus = String.valueOf(httpStatus);
        this.message = message;
    }
}
