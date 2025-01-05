package com.simbrella.dev.user_mgt_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserRequestDto implements Serializable {

    private static final long serialVersionUID= 1L;

    @NotBlank(message = "First Name is required")
    @JsonProperty
    @Schema(description = "first name", example = "Mustapha")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    @JsonProperty
    @Schema(description = "last name", example = "Musa")
    private String lastName;


    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9\\W]).*$",
            message = "Password must contain a number or a symbol and at least one uppercase letter")
    private String password;

    @NotBlank(message = "Email is required")
    @JsonProperty
    @Schema(description = "email", example = "doe@gmail.com")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{9,10}$", message = "Phone number must contain 9 or 10 digits")
    @JsonProperty
    @Schema(description = "phone number", example = "9166099828")
    private String phoneNumber;

}
