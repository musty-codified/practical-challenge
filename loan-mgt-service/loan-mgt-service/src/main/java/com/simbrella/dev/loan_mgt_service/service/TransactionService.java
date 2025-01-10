package com.simbrella.dev.loan_mgt_service.service;

import com.simbrella.dev.loan_mgt_service.dto.request.TransactionDto;

import java.util.List;

public interface TransactionService {
    TransactionDto recordTransactions(TransactionDto transactionDto);

    List<TransactionDto> getTransactionByLoanId(Long loanId);

    TransactionDto generateTransactionStatementByUserId(Long userId);
}
