package com.simbrella.dev.loan_mgt_service.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.simbrella.dev.loan_mgt_service.dto.UserClient;
import com.simbrella.dev.loan_mgt_service.dto.request.LoanRequestDto;
import com.simbrella.dev.loan_mgt_service.dto.request.UpdateLoanRequest;
import com.simbrella.dev.loan_mgt_service.dto.response.LoanDto;
import com.simbrella.dev.loan_mgt_service.dto.response.UserResponseDTO;
import com.simbrella.dev.loan_mgt_service.entity.Loan;
import com.simbrella.dev.loan_mgt_service.exception.LoanNotFoundException;
import com.simbrella.dev.loan_mgt_service.repository.LoanRepository;
import com.simbrella.dev.loan_mgt_service.service.LoanService;
import com.simbrella.dev.loan_mgt_service.util.AppUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;


@Slf4j
@Service
//@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final AppUtil appUtil;
    private final LoanRepository loanRepository;
    private final UserClient userClient;

    public LoanServiceImpl(AppUtil appUtil, LoanRepository loanRepository, UserClient userClient) {
        this.appUtil = appUtil;
        this.loanRepository = loanRepository;
        this.userClient = userClient;
    }

    @Override
    public LoanDto fetchLoanDetailsByUser(Long loanId) {
       Loan loan = loanRepository.findById(loanId)
               .orElseThrow(()-> new LoanNotFoundException("Loan not found", HttpStatus.NOT_FOUND.name()));
        Map<String, Object> responseMap = userClient.fetchLoanDetailsByUser(loan.getUserId());
        UserResponseDTO userResponse = parseResponse(responseMap);
        LoanDto loanDto = appUtil.mapToDto(loan);
        loanDto.setFullName(userResponse.getFirstName() + userResponse.getLastName());
        loanDto.setPhoneNumber(userResponse.getPhoneNumber());
        return loanDto;

    }


    private UserResponseDTO parseResponse(Map<String, Object> response) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(response, UserResponseDTO.class);
    }
    @Override
    public LoanDto applyLoan(LoanRequestDto loanDto) {
        return null;
    }

    @Override
    public LoanDto updateLoan(Long loanId, UpdateLoanRequest updateRequest) {
        return null;
    }
}
