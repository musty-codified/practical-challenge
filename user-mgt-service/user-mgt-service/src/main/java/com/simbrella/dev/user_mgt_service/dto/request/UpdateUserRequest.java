package com.simbrella.dev.user_mgt_service.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class UpdateUserRequest implements Serializable {
    private static final long serialVersionUID= 1L;

    @JsonProperty
    @Schema(description = "first name", example = "Mustapha")
    @NotBlank(message = "First name is required")

    private String firstName;

    @JsonProperty
    @Schema(description = "last name", example = "Musa")
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Pattern(regexp = "^[+]?[0-9]{9,10}$", message = "Phone number must contain 9 or 10 digits")
    @JsonProperty
    @Schema(description = "phone number", example = "9166099828")
    private String phoneNumber;

    @Pattern(regexp = "^(ACTIVE|INACTIVE)$", message = "Status can only be either ACTIVE, INACTIVE")
    private String status;


}
