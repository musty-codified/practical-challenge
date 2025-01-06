package com.simbrella.dev.loan_mgt_service.repository;

import com.simbrella.dev.loan_mgt_service.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUserId(Long userId);

}
