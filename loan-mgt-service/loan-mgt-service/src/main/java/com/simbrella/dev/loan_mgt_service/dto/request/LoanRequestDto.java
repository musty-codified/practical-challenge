package com.simbrella.dev.loan_mgt_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class LoanRequestDto implements Serializable {

    private static final long serialVersionUID= 1L;

    @NotNull(message = "amount is required")
    @Schema(description = "amount", example = "50000")
    @DecimalMin("0.0")
    private BigDecimal amount;

    @NotNull(message = "tenureInMonth is required")
    @Schema(description = "tenureInMonth", example = "12")
    private Integer tenureInMonth;


    @NotNull(message = "User ID is required")
    @Schema(description = "User ID", example = "2")
    private Long userId;


}
