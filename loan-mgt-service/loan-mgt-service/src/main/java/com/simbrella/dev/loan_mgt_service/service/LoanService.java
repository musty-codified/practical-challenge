package com.simbrella.dev.loan_mgt_service.service;


import com.simbrella.dev.loan_mgt_service.dto.request.LoanRequestDto;
import com.simbrella.dev.loan_mgt_service.dto.request.UpdateLoanRequest;
import com.simbrella.dev.loan_mgt_service.dto.response.LoanDto;
import jakarta.validation.Valid;

public interface LoanService {
    LoanDto fetchLoanDetailsByUser (Long loanId);

    LoanDto applyLoan(@Valid LoanRequestDto loanDto);

    LoanDto updateLoan(Long loanId, UpdateLoanRequest updateRequest);
}
