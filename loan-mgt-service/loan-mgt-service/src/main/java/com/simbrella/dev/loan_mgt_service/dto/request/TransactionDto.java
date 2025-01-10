package com.simbrella.dev.loan_mgt_service.dto.request;


import com.simbrella.dev.loan_mgt_service.enums.TransactionType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransactionDto {
    @NotNull(message = "Loan Id is required")
    @Schema(description = "loan Id", example = "7")
    private Long loanId;

    @NotNull(message = "transactionType is required")
    @Schema(description = "transactionType", example = "REPAYMENT")
    @Pattern(regexp = "^(DISBURSEMENT|REPAYMENT)$", message = "TransactionType can only be either DISBURSEMENT or REPAYMENT")
    private String transactionType;

    @NotNull(message = "amount is required")
    @Schema(description = "amount", example = "50000")
    private BigDecimal amount;


}
