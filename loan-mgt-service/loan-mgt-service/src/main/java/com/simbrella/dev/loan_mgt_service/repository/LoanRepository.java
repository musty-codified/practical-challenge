package com.simbrella.dev.loan_mgt_service.repository;

import com.simbrella.dev.loan_mgt_service.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;



public interface LoanRepository extends JpaRepository<Loan, Long> {
}
