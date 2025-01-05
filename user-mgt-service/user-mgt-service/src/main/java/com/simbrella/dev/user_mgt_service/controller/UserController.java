package com.simbrella.dev.user_mgt_service.controller;

import com.simbrella.dev.user_mgt_service.dto.request.UpdateUserRequest;
import com.simbrella.dev.user_mgt_service.dto.request.UserRequestDto;
import com.simbrella.dev.user_mgt_service.dto.response.ApiResponse;
import com.simbrella.dev.user_mgt_service.dto.response.UserResponseDto;
import com.simbrella.dev.user_mgt_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Set;


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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserDetails(@PathVariable(value = "id") @Positive(message = "User ID must be a positive number") Long id) {
        if (id == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>(false, "ID is required", null));
        }
        UserResponseDto user = userService.getUserDetails(id);
        return ResponseEntity.ok().body(new ApiResponse<>(true, "Request Successfully processed", user));
    }

    @PutMapping("/{id}")

    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(@PathVariable("id") Long id,
                                                                          @Validated() @RequestBody UpdateUserRequest updateRequest) throws NoHandlerFoundException {
        if(updateRequest.getPhoneNumber() != null || updateRequest.getStatus() !=null) {
            validator.validate(updateRequest);
            Set<ConstraintViolation<UpdateUserRequest>> violations =
                    validator.validate(updateRequest);
            if (!violations.isEmpty()){
                throw new ConstraintViolationException(violations);
            }
        }
        return ResponseEntity.ok().body(new ApiResponse<>(true, "Request Successfully processed", userService.updateUser(id, updateRequest)));

    }
}
