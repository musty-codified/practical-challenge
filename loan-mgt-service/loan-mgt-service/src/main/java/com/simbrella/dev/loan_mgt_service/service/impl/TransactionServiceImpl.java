package com.simbrella.dev.loan_mgt_service.service.impl;

import com.simbrella.dev.loan_mgt_service.dto.request.TransactionDto;
import com.simbrella.dev.loan_mgt_service.entity.Loan;
import com.simbrella.dev.loan_mgt_service.entity.Transaction;
import com.simbrella.dev.loan_mgt_service.enums.TransactionType;
import com.simbrella.dev.loan_mgt_service.exception.LoanNotFoundException;
import com.simbrella.dev.loan_mgt_service.repository.LoanRepository;
import com.simbrella.dev.loan_mgt_service.repository.TransactionRepository;
import com.simbrella.dev.loan_mgt_service.service.TransactionService;
import com.simbrella.dev.loan_mgt_service.util.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final LoanRepository loanRepository;
    private final AppUtil appUtil;
    @Override
    public String recordTransactions(TransactionDto transactionDto) {

        Loan loan = loanRepository.findById(transactionDto.getLoanId())
                .orElseThrow(()-> new LoanNotFoundException("Transaction not found", HttpStatus.NOT_FOUND.name()));
        if (transactionDto.getTransactionType() == TransactionType.REPAYMENT
                && transactionDto.getAmount().compareTo(loan.getAmount())> 0){
            throw new IllegalArgumentException("Payment amount exceeds outstanding balance");

        }

        if (transactionDto.getTransactionType() == TransactionType.REPAYMENT){
            loan.setAmount(loan.getAmount().subtract(transactionDto.getAmount()));
            loanRepository.save(loan);
        }

        Transaction transaction = Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .amount(transactionDto.getAmount())
                .loanId(transactionDto.getLoanId())
                .build();

        transactionRepository.save(transaction);
        return "";
    }

    @Override
    public List<TransactionDto> getTransactionVyLoanId(Long loanId) {
       List<Transaction> transactions = transactionRepository.findByLoanId(loanId);
        return transactions.stream().map(appUtil::mapToTransactionDto).collect(Collectors.toList());
    }
}
