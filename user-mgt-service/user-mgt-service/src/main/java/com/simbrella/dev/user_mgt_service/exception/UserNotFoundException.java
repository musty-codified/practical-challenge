package com.simbrella.dev.user_mgt_service.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserNotFoundException extends RuntimeException {
    private String message;
    private String httpStatus;

    public UserNotFoundException(String message, String httpStatus) {
        super(message);
        this.httpStatus = String.valueOf(httpStatus);
        this.message = message;
    }
}
