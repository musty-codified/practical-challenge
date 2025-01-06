package com.simbrella.dev.loan_mgt_service.repository;

import com.simbrella.dev.loan_mgt_service.entity.Loan;
import com.simbrella.dev.loan_mgt_service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Loan> {
    List<Transaction> findByLoanId(Long loanId);
}
