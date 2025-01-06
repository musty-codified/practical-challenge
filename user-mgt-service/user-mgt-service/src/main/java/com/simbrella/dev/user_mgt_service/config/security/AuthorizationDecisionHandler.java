package com.simbrella.dev.user_mgt_service.config.security;

import lombok.Getter;
import org.springframework.security.authorization.AuthorizationDecision;

@Getter
public class AuthorizationDecisionHandler extends AuthorizationDecision {
    private final String message;
    public AuthorizationDecisionHandler(boolean granted, String message) {
        super(granted);
        this.message = message;    }
}
