package com.simbrella.dev.loan_mgt_service.util;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simbrella.dev.loan_mgt_service.dto.response.LoanDto;
import com.simbrella.dev.loan_mgt_service.entity.Loan;

import org.springframework.stereotype.Component;


@Component
public class AppUtil {

    public LoanDto mapToDto(Loan loanEntity) {
        return LoanDto.builder()
                .id(loanEntity.getId())
                .amount(loanEntity.getAmount())
                .tenureInMonths(loanEntity.getTenureInMonth())
                .createdAt(loanEntity.getCreatedAt())
                .updatedAt(loanEntity.getUpdatedAt())
                .loanStatus(loanEntity.getStatus().getValue())
                .build();
    }

    public ObjectMapper getMapper(){
        return new ObjectMapper();
    }


}
