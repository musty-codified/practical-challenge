package com.simbrella.dev.loan_mgt_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class LoanRequestDto implements Serializable {

    private static final long serialVersionUID= 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "amount is required")
    @JsonProperty
    @Schema(description = "amount", example = "5000.0")
    private BigDecimal amount;

    @Column(nullable = false)
    @NotBlank(message = "tenureInMonth is required")
    @JsonProperty
    @Schema(description = "tenureInMonth", example = "3")
    private Integer tenureInMonth;

    @Column(nullable = false)
    @NotBlank(message = "interestRate is required")
    @JsonProperty
    private BigDecimal interestRate;


}
