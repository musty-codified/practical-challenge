package com.simbrella.dev.user_mgt_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class LoginRequestDto implements Serializable {
    @NotBlank
    @NotNull
    @Email
    private final String email;

    @NotBlank
    @Size(max = 128)
    private final String password;
}
