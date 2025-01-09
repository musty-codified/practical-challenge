package com.simbrella.dev.loan_mgt_service.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
@Component
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor{
    @Override
    public void apply(RequestTemplate requestTemplate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getCredentials() instanceof String) {
            String jwtToken = (String) authentication.getCredentials();
            requestTemplate.header("Authorization", "Bearer " + jwtToken);
            log.info("Feign request headers:{}", requestTemplate.headers());

        } else {
            log.warn("No token available in SecurityContext");
        }

    }
}
