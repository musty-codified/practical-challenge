package com.simbrella.dev.user_mgt_service.service;

import com.simbrella.dev.user_mgt_service.dto.request.UserRequestDto;
import com.simbrella.dev.user_mgt_service.dto.response.UserResponseDto;

public interface UserService {
    UserResponseDto createUser(UserRequestDto userDto);
    UserResponseDto getUserDetails(long id);
}
