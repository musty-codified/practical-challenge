package com.simbrella.dev.loan_mgt_service.dto.response;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanDto {
    private Long id;
    private BigDecimal amount;
    private Integer tenureInMonths;
    private String fullName;
    private String phoneNumber;
    @JsonIgnore
    private String loanStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
