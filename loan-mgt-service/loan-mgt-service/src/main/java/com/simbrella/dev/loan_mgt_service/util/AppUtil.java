package com.simbrella.dev.loan_mgt_service.util;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simbrella.dev.loan_mgt_service.dto.request.TransactionDto;
import com.simbrella.dev.loan_mgt_service.dto.response.LoanDto;
import com.simbrella.dev.loan_mgt_service.entity.Loan;

import com.simbrella.dev.loan_mgt_service.entity.Transaction;
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


    public TransactionDto mapToTransactionDto(Transaction transaction) {
        return TransactionDto.builder()
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType())
                .loanId(transaction.getLoanId())
                .build();
    }

    public ObjectMapper getMapper(){
        return new ObjectMapper();
    }


}
