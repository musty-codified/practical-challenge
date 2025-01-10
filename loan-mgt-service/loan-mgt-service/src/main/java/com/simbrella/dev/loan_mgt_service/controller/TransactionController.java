package com.simbrella.dev.loan_mgt_service.controller;

import com.simbrella.dev.loan_mgt_service.dto.request.TransactionDto;
import com.simbrella.dev.loan_mgt_service.dto.response.ApiResponse;
import com.simbrella.dev.loan_mgt_service.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Slf4j
@Validated
public class TransactionController {
    private final TransactionService loanService;

    @Operation(summary = "This endpoint allows processing of loan transactions")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Request successfully processed")
    @PostMapping()
    public ResponseEntity<ApiResponse<TransactionDto>> recordTransaction(@Valid @RequestBody TransactionDto transactionDto) {
        return ResponseEntity.ok().body(new ApiResponse<>(true, "Request Successfully processed", loanService.recordTransactions(transactionDto)));
    }

    @Operation(summary = "This endpoint allows the retrieval of loan transactions history")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Request successfully processed")
    @GetMapping("/{loanId}")
    public ResponseEntity<ApiResponse<List<TransactionDto>>> getTransactionsByLoanId(@PathVariable("loanId") Long loanId) {
        return ResponseEntity.ok().body(new ApiResponse<>(true, "Request Successfully processed", loanService.getTransactionByLoanId(loanId)));
    }

    @Operation(summary = "This endpoint allows processing of loan transactions")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Request successfully processed")
    @GetMapping("/{userId}/statement")
    public ResponseEntity<ApiResponse<TransactionDto>> generateTransactionStatement(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok().body(new ApiResponse<>(true, "Request Successfully processed", loanService.generateTransactionStatementByUserId(userId)));
    }
}
