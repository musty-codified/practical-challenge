package com.simbrella.dev.loan_mgt_service.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class UrlAccessDecisionManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Value("${server.servlet.context-path}")
    private String contextPath;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final com.simbrella.dev.loan_mgt_service.config.security.PermissionConfig permissionConfig;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        List<String> whitelistedUrls = Arrays.asList("/swagger-ui/**", "/v3/api-docs/**", "/**");

        String requestedUrl = object.getRequest().getRequestURI().substring(contextPath.length()).toLowerCase();
        String requestedMethod = object.getRequest().getMethod();
        if (whitelistedUrls.stream().anyMatch(e->pathMatcher.match(e, requestedUrl))){
            return new AuthorizationDecision(true);
        }
        if (authentication.get().getAuthorities().parallelStream()
                .map(GrantedAuthority::getAuthority)
                .map(String::toLowerCase)
                .anyMatch(authority -> authority.equals("user.edit") || authority.equals("user.read"))) {
            return new AuthorizationDecision(true);
        }
        boolean match = authentication.get().getAuthorities().parallelStream()
                .map(GrantedAuthority::getAuthority)
                .map(String::toLowerCase)
                .anyMatch(permission -> matchesPermission(permission, requestedUrl, requestedMethod));

        if (match) {
            return new AuthorizationDecision(true);
        }else{
            String message = "Access denied. No matching authority in user found for the requested URL and method.";
            log.debug("{} User: {}, URL: {}, Method: {}", message, authentication.get().getName(), requestedUrl, requestedMethod);
            return new AuthorizationDecisionHandler(false, message);
        }    }

    @Override
    public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        AuthorizationManager.super.verify(authentication, object);
    }

    private boolean matchesPermission(String permission, String requestedUrl, String requestMethod) {
        log.info("Permission List :{}", permissionConfig.getPermissions());
        return permissionConfig.getPermissions().stream()
                .filter(e->e.getPermission().equals(permission))
                .anyMatch(mapping->mapping.getMethods().contains(requestMethod) &&
                        mapping.getPatterns().stream().anyMatch(pattern->pathMatcher.match(pattern, requestedUrl)));
    }
}
