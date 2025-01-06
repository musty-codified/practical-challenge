package com.simbrella.dev.user_mgt_service.service;

import com.simbrella.dev.user_mgt_service.dto.request.LoginRequestDto;
import com.simbrella.dev.user_mgt_service.dto.response.LoginResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    LoginResponseDto login (LoginRequestDto adminLoginRequestDto, HttpServletRequest request);

}
