package com.simbrella.dev.loan_mgt_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    PENDING("PENDING"),
    APPROVED("APPROVED"),
    DISBURSED("DISBURSED"),
    REJECTED("REJECTED");

    private final String value;


}
