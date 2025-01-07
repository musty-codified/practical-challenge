package com.simbrella.dev.user_mgt_service.config.security;

import com.simbrella.dev.user_mgt_service.dto.response.ApiResponse;
import com.simbrella.dev.user_mgt_service.util.AppUtil;
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

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
    private final AppUtil appUtil;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        String tokenPassed = request.getHeader(HttpHeaders.AUTHORIZATION);
        String message = authException.getMessage() + ":: Attempt to access the protected URL: " + request.getRequestURI() + " is Denied";

        log.error("Message: {} : token passed is {}", message, tokenPassed);
        log.error("token: {}, Cause: {}",  tokenPassed, authException.getMessage());

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
