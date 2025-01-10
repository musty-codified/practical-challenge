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

       try {
           log.info("Calling fetchUserDetails on userClient");
           Map<String, Object> responseMap = userClient.fetchUserDetails(userId);
           UserResponseDTO userResponse = parseResponse(responseMap);
           return loans.stream().map(loan -> {
                       LoanDto loanDto = appUtil.mapToDto(loan);
                       loanDto.setFullName(userResponse.getFirstName() +  "" + userResponse.getLastName());
                       loanDto.setPhoneNumber(userResponse.getPhoneNumber());
                       loanDto.setLoanStatus(String.valueOf(loan.getStatus()));
                       loan.setInterestRate(loan.getInterestRate());
                       loan.setAmount(loan.getAmount());
                       loan.setTenureInMonth(loan.getTenureInMonth());
                       return loanDto;
                   })
                   .collect(Collectors.toList());
       }catch (feign.FeignException feignException){
           ObjectMapper objectMapper = new ObjectMapper();
           String body = feignException.contentUTF8();
           log.warn("body of feign exception:{}", body);

           Map<String, Object> errorResponse = null;
           try {
               errorResponse = objectMapper.readValue(body, new TypeReference<HashMap<String, Object>>() {
               });
           } catch (JsonProcessingException e) {
               throw new RuntimeException(e);
           }
           String errorMessage = (String) errorResponse.getOrDefault("message", "No users found");
           if (feignException.status() == 403){
               log.error("Unauthorized access: {}", feignException.getMessage());
               throw new UnAuthorizedException(feignException.getMessage());
           } else if (feignException.status() == 404) {
               throw new ResourceNotFoundException(errorMessage, null);
           } else if (feignException.status() == 400) {
               throw new IllegalArgumentException(feignException.getMessage());
           }

       } catch (Exception e) {
           log.error("Exception occurred while retrieving user details: {}", e.getMessage(), e);
           throw new RuntimeException(String.format("Feign encountered an error fetching user details: %s %s", e.getMessage(), e));
       }

        return null;
    }

    @Override
    public LoanDto applyLoan(LoanRequestDto loanDto) {
        if(loanDto.getUserId() == null) {
            throw new IllegalArgumentException("User Id cannot be null");
        }

        try {
            Map<String, Object> responseMap = userClient.fetchUserDetails(loanDto.getUserId());
            UserResponseDTO userResponse = parseResponse(responseMap);

            List<Loan> pendingLoans = loanRepository.findByUserIdAndStatus(loanDto.getUserId(), Status.PENDING);
            if (!pendingLoans.isEmpty()) {
                throw new IllegalStateException("User already has a pending loan application.");

            }
            Loan loan = Loan.builder()
                    .amount(loanDto.getAmount())
                    .createdAt(LocalDateTime.now())
                    .interestRate(computeInterestRate(loanDto.getAmount(), loanDto.getTenureInMonth()))
                    .tenureInMonth(loanDto.getTenureInMonth())
                    .status(Status.PENDING)
                    .userId(loanDto.getUserId())
                    .build();

            try {
                Loan savedLoan = loanRepository.save(loan);
                return LoanDto.builder()
                        .id(savedLoan.getId())
                        .amount(savedLoan.getAmount())
                        .loanStatus(Status.PENDING.getValue())
                        .tenureInMonths(savedLoan.getTenureInMonth())
                        .createdAt(savedLoan.getCreatedAt())
                        .fullName(userResponse.getFirstName() + "," + userResponse.getLastName())
                        .phoneNumber(userResponse.getPhoneNumber())
                        .updatedAt(LocalDateTime.now())
                        .build();
            } catch (Exception e) {
                throw new RuntimeException("Error saving loan: " + e.getMessage(), e);
            }
        }catch (feign.FeignException feignException){
            ObjectMapper objectMapper = new ObjectMapper();
            String body = feignException.contentUTF8();

            Map<String, Object> errorResponse = null;
            try {
                errorResponse = objectMapper.readValue(body, new TypeReference<HashMap<String, Object>>() {
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            String errorMessage = (String) errorResponse.getOrDefault("message", "No users found");
            if (feignException.status() == 403){
                throw new UnAuthorizedException(feignException.getMessage());
            } else if (feignException.status() == 404) {
                throw new ResourceNotFoundException(errorMessage, null);
            } else if (feignException.status() == 400) {
                throw new IllegalArgumentException(feignException.getMessage());
            }

        } catch (Exception e) {
            log.error("Exception occurred while retrieving user details: {}", e.getMessage(), e);
            throw new RuntimeException(String.format("Feign encountered an error fetching user details: %s %s", e.getMessage(), e));
        }
        return null;
    }


   //Predefined interest rate
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
        if (response == null || !Boolean.TRUE.equals(response.get("success"))){
            throw new IllegalStateException("Failed to fetch user details");
        }
           @SuppressWarnings("unchecked")
           Map<String, Object> data = (Map<String, Object>) response.get("data");
        if (data == null){
            throw new IllegalStateException("Missing data");
        }
          return UserResponseDTO.builder()
               .id(((Number) data.get("id")).longValue())
               .firstName((String) data.get("firstName"))
               .lastName((String) data.get("lastName"))
               .phoneNumber((String) data.get("phoneNumber"))
               .email((String) data.get("email"))
               .status((String) data.get("status"))
               .createdAt(LocalDateTime.parse((String) data.get("createdAt")))
               .updatedAt(LocalDateTime.parse((String) data.get("updatedAt")))
               .build();
    }
}
