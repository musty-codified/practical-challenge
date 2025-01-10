package com.simbrella.dev.loan_mgt_service.repository;

import com.simbrella.dev.loan_mgt_service.entity.Loan;
import com.simbrella.dev.loan_mgt_service.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface LoanRepository extends JpaRepository<Loan, Long> {
    Optional<List<Loan> >findByUserId(Long userId);
//    Optional<List<Loan> >findByUserId(Long userId);
    List<Loan>findByUserIdAndStatus(Long userId, Status status);

}
