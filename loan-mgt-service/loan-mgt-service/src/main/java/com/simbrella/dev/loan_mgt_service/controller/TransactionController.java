package com.simbrella.dev.loan_mgt_service.controller;
import com.simbrella.dev.loan_mgt_service.dto.request.TransactionDto;
import com.simbrella.dev.loan_mgt_service.dto.response.ApiResponse;
import com.simbrella.dev.loan_mgt_service.service.TransactionService;
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
        @PostMapping()
        public ResponseEntity<ApiResponse<String>> recordTransaction(@Valid @RequestBody TransactionDto transactionDto) {
            return ResponseEntity.ok().body(new ApiResponse<>(true, "Request Successfully processed", loanService.recordTransactions(transactionDto)));
        }

    @GetMapping("/transactions/loanId")
    public ResponseEntity<ApiResponse<List<TransactionDto>>> getTransactionsByLoanId(@PathVariable("loanId") Long loanId) {
        return ResponseEntity.ok().body(new ApiResponse<>(true, "Request Successfully processed", loanService.getTransactionVyLoanId(loanId)));
    }
}
