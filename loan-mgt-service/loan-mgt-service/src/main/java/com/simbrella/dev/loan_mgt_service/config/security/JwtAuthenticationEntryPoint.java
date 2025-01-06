package com.simbrella.dev.loan_mgt_service.config.security;

import com.simbrella.dev.loan_mgt_service.dto.response.ApiResponse;
import com.simbrella.dev.loan_mgt_service.util.AppUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Map;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
    private final AppUtil appUtil;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        String tokenPassed = request.getHeader(HttpHeaders.AUTHORIZATION);
        final Map<Pattern, String> ERROR_PATTERNS = Map.of(
                Pattern.compile(".*expired.*"), "Token has expired",
                Pattern.compile(".*period characters.*"), "JWT strings must contain exactly 2 period characters",
                Pattern.compile(".*Unable to verify RSA.*"), "Unable to verify RSA public key",
                Pattern.compile(".*signature.*"), "Invalid JWT token signature",
                Pattern.compile(".*malformed.*"), "Malformed JWT token",
                Pattern.compile(".*unsupported.*"), "Unsupported JWT token",
                Pattern.compile(".*illegal argument.*"), "Illegal argument in JWT token"
        );

        String errorMessage = ERROR_PATTERNS.entrySet().stream()
                .filter(entry -> entry.getKey().matcher(authException.getMessage().toLowerCase()).find())
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(authException.getMessage());

        String message = "Access to " + request.getServletPath() + " denied: " + errorMessage;

        log.error("Message: {} : token passed is {}", message, tokenPassed);

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        PrintWriter writer = response.getWriter();
        ApiResponse res = new ApiResponse<>(false, "Access to protected resource denied", null);
        writer.print(appUtil.getMapper().writeValueAsString(res));
        writer.flush();
        writer.close();
    }
}
