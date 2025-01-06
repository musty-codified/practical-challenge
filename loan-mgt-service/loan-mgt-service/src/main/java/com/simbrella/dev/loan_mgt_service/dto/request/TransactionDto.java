package com.simbrella.dev.loan_mgt_service.dto.request;


import com.simbrella.dev.loan_mgt_service.enums.TransactionType;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransactionDto {

    private TransactionType transactionType;

    private BigDecimal amount;

    private Long loanId;

}
