package com.simbrella.dev.loan_mgt_service.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoanNotFoundException extends RuntimeException {
    private String message;
    private String httpStatus;

    public LoanNotFoundException(String message, String httpStatus) {
        super(message);
        this.httpStatus = String.valueOf(httpStatus);
        this.message = message;
    }
}
