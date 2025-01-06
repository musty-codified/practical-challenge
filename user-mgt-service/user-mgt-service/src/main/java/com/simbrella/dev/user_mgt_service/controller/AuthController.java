package com.simbrella.dev.user_mgt_service.controller;

import com.simbrella.dev.user_mgt_service.dto.request.LoginRequestDto;
import com.simbrella.dev.user_mgt_service.dto.response.ApiResponse;
import com.simbrella.dev.user_mgt_service.dto.response.LoginResponseDto;
import com.simbrella.dev.user_mgt_service.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody @Validated LoginRequestDto adminLoginRequestDto, HttpServletRequest request) {
        return ResponseEntity.ok().body(new ApiResponse<>(true, "Login Successful", authService.login(adminLoginRequestDto, request)));
    }

}
