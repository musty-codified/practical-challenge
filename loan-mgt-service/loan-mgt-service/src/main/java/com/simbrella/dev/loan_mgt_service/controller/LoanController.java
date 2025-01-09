package com.simbrella.dev.loan_mgt_service.controller;

import com.simbrella.dev.loan_mgt_service.dto.request.LoanRequestDto;
import com.simbrella.dev.loan_mgt_service.dto.request.UpdateLoanRequest;
import com.simbrella.dev.loan_mgt_service.dto.response.ApiResponse;
import com.simbrella.dev.loan_mgt_service.dto.response.LoanDto;
import com.simbrella.dev.loan_mgt_service.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;


@Tag(name = "loan-mgt-endpoint", description = "These endpoints exposes loan-management-API")
@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
@Slf4j
@Validated
public class LoanController {

    private final LoanService loanService;
    private final LocalValidatorFactoryBean validator;
    @Operation(summary = "Request to apply for loan")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Request successfully processed")
    @PostMapping()
    public ResponseEntity<ApiResponse<LoanDto>> applyForLoan(@Valid @RequestBody LoanRequestDto loanDto) {
        LoanDto response = loanService.applyLoan(loanDto);
        return ResponseEntity.ok().body(new ApiResponse<>(true, "Request Successfully processed", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<List<LoanDto>>> getLoanDetailsByUser(@PathVariable(value = "id") @Positive(message = "Loan ID must be a positive") Long id) {
        if (id == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>(false, "ID is required", null));
        }
        return ResponseEntity.ok().body(new ApiResponse<>(true, "Request Successfully processed", loanService.fetchLoanDetailsByUser(id)));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<LoanDto>> updateLoanStatus(@PathVariable("id") Long loanId,
                                                                 @Validated() @RequestBody UpdateLoanRequest updateRequest)  {
        return ResponseEntity.ok().body(new ApiResponse<>(true, "Request Successfully processed", loanService.updateLoan(loanId, updateRequest)));

    }

}
