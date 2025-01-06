package com.simbrella.dev.loan_mgt_service.dto;

import com.simbrella.dev.loan_mgt_service.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "user-mgt-service", url = "${user.url}", configuration = FeignClientConfig.class)
public interface UserClient {
    @GetMapping("/{userId}")
    Map<String, Object> fetchLoanDetailsByUser(@PathVariable("userId") Long userId);
}
