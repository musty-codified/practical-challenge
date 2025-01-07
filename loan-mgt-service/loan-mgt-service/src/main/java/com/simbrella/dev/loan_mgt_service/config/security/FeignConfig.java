package com.simbrella.dev.loan_mgt_service.config.security;


import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//public class FeignConfig {
//    @Bean
//    public RequestInterceptor requestInterceptor(){
//
//        return requestTemplate -> {
//            String token = "YOUR_JWT_TOKEN";
//            requestTemplate.header("Authorization", "Bearer " + token);
//        };
//    }
//}
