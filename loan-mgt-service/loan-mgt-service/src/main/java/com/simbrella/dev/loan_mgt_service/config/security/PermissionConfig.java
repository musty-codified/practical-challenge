package com.simbrella.dev.loan_mgt_service.config.security;


import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "permissions")
@EnableConfigurationProperties
public class PermissionConfig {
    private List<PermissionMapping> permissions = new ArrayList<>();

    @PostConstruct
    public void validate(){
        if (permissions == null || permissions.isEmpty()){
            throw new IllegalArgumentException("Permissions are not loaded. Check application.yml configuration");
        }
    }
}
