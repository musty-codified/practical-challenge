package com.simbrella.dev.loan_mgt_service.enums;

import lombok.Getter;

@Getter
public enum TransactionType {

    DISBURSEMENT("DISBURSEMENT"),
    REPAYMENT("REPAYMENT");
    private final String transactionType;
    TransactionType(String transactionType){
        this.transactionType = transactionType;
    }

}
