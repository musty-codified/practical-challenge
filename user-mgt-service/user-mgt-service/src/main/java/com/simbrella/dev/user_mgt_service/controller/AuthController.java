package com.simbrella.dev.user_mgt_service.controller;

import com.simbrella.dev.user_mgt_service.dto.request.LoginRequestDto;
import com.simbrella.dev.user_mgt_service.dto.response.ApiResponse;
import com.simbrella.dev.user_mgt_service.dto.response.LoginResponseDto;
import com.simbrella.dev.user_mgt_service.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Generates a JWT token upon successful login that will be used for Authorization")
    @ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200", description = "Successful login. Returns a JWT token in the response body",
                            content = @Content(schema = @Schema(implementation = LoginResponseDto.class), mediaType = "application/json"),
                            headers = {@Header(name = "authorization", description = "Bearer <JWT value here>", schema = @Schema(type = "string"))})
            }
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@Validated @RequestBody LoginRequestDto adminLoginRequestDto, HttpServletRequest request) {
        return ResponseEntity.ok().body(new ApiResponse<>(true, "Login Successful", authService.login(adminLoginRequestDto, request)));
    }

}
