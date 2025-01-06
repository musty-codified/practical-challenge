package com.simbrella.dev.user_mgt_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class UserMgtServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserMgtServiceApplication.class, args);
	}

}
