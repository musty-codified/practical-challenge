package com.simbrella.dev.loan_mgt_service.service.impl;
import com.simbrella.dev.loan_mgt_service.dto.request.TransactionDto;
import com.simbrella.dev.loan_mgt_service.entity.Loan;
import com.simbrella.dev.loan_mgt_service.entity.Transaction;
import com.simbrella.dev.loan_mgt_service.enums.TransactionType;
import com.simbrella.dev.loan_mgt_service.exception.ResourceNotFoundException;
import com.simbrella.dev.loan_mgt_service.repository.LoanRepository;
import com.simbrella.dev.loan_mgt_service.repository.TransactionRepository;
import com.simbrella.dev.loan_mgt_service.service.TransactionService;
import com.simbrella.dev.loan_mgt_service.util.AppUtil;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final LoanRepository loanRepository;
    private final AppUtil appUtil;
    @Override
    public TransactionDto recordTransactions(TransactionDto transactionDto) {

        Loan loan = loanRepository.findById(transactionDto.getLoanId())
                .orElseThrow(()-> new ResourceNotFoundException("Loan not found", HttpStatus.NOT_FOUND.name()));

        if (TransactionType.valueOf(transactionDto.getTransactionType()) == TransactionType.REPAYMENT){
            validateRepayment(loan, transactionDto.getAmount());
           loan.setAmount(loan.getAmount().subtract(transactionDto.getAmount()));
        } else if (TransactionType.valueOf(transactionDto.getTransactionType())== TransactionType.DISBURSEMENT){
            validateDisbursement(loan, transactionDto.getAmount());
            loan.setAmount(loan.getAmount().add(transactionDto.getAmount()));
        } else {
            throw new IllegalArgumentException("Invalid transaction type");
        }

        loanRepository.save(loan);

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.valueOf(transactionDto.getTransactionType()))
                .amount(transactionDto.getAmount())
                .loanId(transactionDto.getLoanId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return appUtil.mapToTransactionDto(transactionRepository.save(transaction));
    }

    private void validateRepayment(Loan loan, @NotNull(message = "amount is required") BigDecimal amount) {
           if (loan.getAmount().compareTo(amount) > 0){
               throw new IllegalArgumentException("Payment amount exceeds outstanding balance");
           }
    }

    private void validateDisbursement(Loan loan, @NotNull(message = "amount is required") BigDecimal amount) {
        if (loan.getAmount().compareTo(amount) >= 0){
            throw new IllegalArgumentException("Disbursement amount exceeds approved loan amount");
        }
    }

    @Override
    public List<TransactionDto> getTransactionByLoanId(Long loanId) {
       List<Transaction> transactions = transactionRepository.findByLoanId(loanId);
        return transactions.stream().map(appUtil::mapToTransactionDto).collect(Collectors.toList());
    }

    @Override
    public TransactionDto generateTransactionStatementByUserId(Long userId) {
        return null;
    }
}
