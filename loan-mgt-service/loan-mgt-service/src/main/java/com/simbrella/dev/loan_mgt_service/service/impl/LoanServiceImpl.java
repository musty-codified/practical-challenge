package com.simbrella.dev.loan_mgt_service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simbrella.dev.loan_mgt_service.dto.UserClient;
import com.simbrella.dev.loan_mgt_service.dto.request.LoanRequestDto;
import com.simbrella.dev.loan_mgt_service.dto.request.UpdateLoanRequest;
import com.simbrella.dev.loan_mgt_service.dto.response.LoanDto;
import com.simbrella.dev.loan_mgt_service.dto.response.UserResponseDTO;
import com.simbrella.dev.loan_mgt_service.entity.Loan;
import com.simbrella.dev.loan_mgt_service.enums.Status;
import com.simbrella.dev.loan_mgt_service.exception.LoanNotFoundException;
import com.simbrella.dev.loan_mgt_service.exception.ResourceNotFoundException;
import com.simbrella.dev.loan_mgt_service.exception.UnAuthorizedException;
import com.simbrella.dev.loan_mgt_service.repository.LoanRepository;
import com.simbrella.dev.loan_mgt_service.service.LoanService;
import com.simbrella.dev.loan_mgt_service.util.AppUtil;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final AppUtil appUtil;
    private final LoanRepository loanRepository;
    private final UserClient userClient;

    @Override
    public List<LoanDto> fetchLoanDetailsByUser(Long userId) {
       List<Loan> loans = loanRepository.findByUserId(userId)
               .orElseThrow(()-> new LoanNotFoundException("No loan found for this user id", HttpStatus.NOT_FOUND.name()));

//       try {
           log.info("Calling fetchUserDetails on userClient");
           Map<String, Object> responseMap = userClient.fetchUserDetails(userId);
           UserResponseDTO userResponse = parseResponse(responseMap);
           return loans.stream().map(loan -> {
                       LoanDto loanDto = appUtil.mapToDto(loan);
                       loanDto.setFullName(userResponse.getFirstName() + userResponse.getLastName());
                       loanDto.setPhoneNumber(userResponse.getPhoneNumber());
                       return loanDto;
                   })
                   .collect(Collectors.toList());
//       }catch (feign.FeignException feignException){
//           ObjectMapper objectMapper = new ObjectMapper();
//           String body = feignException.contentUTF8();
//           log.warn("body of feign exception:{}", body);
//
//           Map<String, Object> errorResponse = null;
//           try {
//               errorResponse = objectMapper.readValue(body, new TypeReference<HashMap<String, Object>>() {
//               });
//           } catch (JsonProcessingException e) {
//               throw new RuntimeException(e);
//           }
//
//           String errorMessage = (String) errorResponse.getOrDefault("message", "No users found");
//
//           if (feignException.status() == 403){
//               log.error("Unauthorized access: {}", feignException.getMessage());
//
//               throw new UnAuthorizedException(feignException.getMessage());
//           } else if (feignException.status() == 404) {
//               throw new ResourceNotFoundException(errorMessage, null);
//           } else if (feignException.status() == 400) {
//               throw new IllegalArgumentException(feignException.getMessage());
//           }
//
//       } catch (Exception e) {
//           log.error("Exception occurred while retrieving user details: {}", e.getMessage(), e);
//           throw new RuntimeException(String.format("Feign encountered an error fetching user details: %s %s", e.getMessage(), e));
//       }
//
//        return null;
    }

    @Override
    public LoanDto applyLoan(LoanRequestDto loanDto) {
        Map<String, Object> responseMap = userClient.fetchUserDetails(loanDto.getUserId());
        UserResponseDTO userResponse = parseResponse(responseMap);
        if(loanDto.getUserId() != null) {
            Loan loan = loanRepository.findById(loanDto.getUserId()).orElse(
                    Loan.builder()
                            .id(loanDto.getUserId())
                            .amount(BigDecimal.ZERO)
                            .interestRate(BigDecimal.ZERO)
                            .build());
            loan.setAmount(loanDto.getAmount());
            loan.setInterestRate(computeInterestRate(loanDto.getAmount(), loanDto.getTenureInMonth()));
            loan.setCreatedAt(LocalDateTime.now());
            loan.setStatus(Status.PENDING);

            Loan savedLoan = loanRepository.save(loan);

            return LoanDto.builder()
                    .id(savedLoan.getId())
                    .amount(savedLoan.getAmount())
                    .loanStatus(Status.PENDING.getValue())
                    .tenureInMonths(savedLoan.getTenureInMonth())
                    .createdAt(savedLoan.getCreatedAt())
                    .fullName(userResponse.getFirstName() + userResponse.getLastName())
                    .phoneNumber(userResponse.getPhoneNumber())
                    .updatedAt(LocalDateTime.now())
                    .build();
        }

        return null;
    }

    private BigDecimal computeInterestRate(@NotBlank(message = "amount is required") BigDecimal amount,
                                           @NotBlank(message = "tenureInMonth is required") Integer tenureInMonth) {
        BigDecimal baseRate = BigDecimal.valueOf(0.05);
        BigDecimal additionalRate = BigDecimal.valueOf(0.01).multiply(BigDecimal.valueOf(tenureInMonth / 12));
        return baseRate.add(additionalRate).multiply(amount);
    }

    @Override
    public LoanDto updateLoan(Long loanId, UpdateLoanRequest updateRequest) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(()-> new LoanNotFoundException("Loan not found", HttpStatus.NOT_FOUND.name()));
        loan.setStatus(Status.valueOf(updateRequest.getStatus()));
        loanRepository.save(loan);
        return appUtil.mapToDto(loan);
    }

    private UserResponseDTO parseResponse(Map<String, Object> response) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(response, UserResponseDTO.class);
    }
}
