package com.simbrella.dev.loan_mgt_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class LoanAlreadyExistException extends RuntimeException{
    public LoanAlreadyExistException(String message) {
        super(message);
        this.status = HttpStatus.CONFLICT;
    }

    private final HttpStatus status;
}
