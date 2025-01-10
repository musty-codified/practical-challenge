package com.simbrella.dev.loan_mgt_service.service;


import com.simbrella.dev.loan_mgt_service.dto.request.LoanRequestDto;
import com.simbrella.dev.loan_mgt_service.dto.request.UpdateLoanRequest;
import com.simbrella.dev.loan_mgt_service.dto.response.LoanDto;

import java.util.List;

public interface LoanService {
    LoanDto applyLoan(LoanRequestDto loanDto);
    List<LoanDto> fetchLoanDetailsByUser (Long loanId);
    LoanDto updateLoan(Long loanId, UpdateLoanRequest updateRequest);
}
