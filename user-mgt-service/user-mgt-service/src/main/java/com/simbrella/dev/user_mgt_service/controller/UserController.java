package com.simbrella.dev.user_mgt_service.controller;

import com.simbrella.dev.user_mgt_service.dto.request.UserRequestDto;
import com.simbrella.dev.user_mgt_service.dto.response.ApiResponse;
import com.simbrella.dev.user_mgt_service.dto.response.UserResponseDto;
import com.simbrella.dev.user_mgt_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "user-mgt-endpoint", description = "These endpoints exposes user-management-API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserService userService;
    private final LocalValidatorFactoryBean validator;
    @Operation(summary = "Create a new user account")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Request successfully processed")
    @PostMapping()
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(@Valid @RequestBody UserRequestDto userDto) {
        return ResponseEntity.ok().body(new ApiResponse<>(true, "Request Successfully processed", userService.createUser(userDto)));
    }
}
