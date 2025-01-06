package com.simbrella.dev.loan_mgt_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.simbrella.dev.loan_mgt_service.dto")
public class LoanMgtServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoanMgtServiceApplication.class, args);
	}

}
