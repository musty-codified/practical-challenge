package com.simbrella.dev.user_mgt_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException(String message) {
        super(message);
        this.status = HttpStatus.CONFLICT;
    }

    private final HttpStatus status;
}
