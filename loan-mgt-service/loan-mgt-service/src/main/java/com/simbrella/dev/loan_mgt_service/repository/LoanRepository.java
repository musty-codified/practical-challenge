package com.simbrella.dev.loan_mgt_service.repository;

import com.simbrella.dev.loan_mgt_service.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface LoanRepository extends JpaRepository<Loan, Long> {
    Optional<List<Loan> >findByUserId(Long userId);

}
