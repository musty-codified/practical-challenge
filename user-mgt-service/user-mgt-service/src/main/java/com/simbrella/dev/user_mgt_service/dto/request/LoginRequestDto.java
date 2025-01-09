package com.simbrella.dev.user_mgt_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "email", example = "musty@gmail.com")
    private final String email;

    @NotBlank
    @Size(max = 128)
    @Schema(description = "password", example = "0bv20S!ecQgd")
    private final String password;


}
