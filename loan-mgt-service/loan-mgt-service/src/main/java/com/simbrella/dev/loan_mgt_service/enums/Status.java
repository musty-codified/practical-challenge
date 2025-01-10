package com.simbrella.dev.loan_mgt_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    PENDING("PENDING"),
    APPROVED("APPROVED"),
    REPAID("REPAID"),
    REJECTED("REJECTED");

    private final String value;


}
