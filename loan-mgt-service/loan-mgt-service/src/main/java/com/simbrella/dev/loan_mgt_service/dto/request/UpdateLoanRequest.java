package com.simbrella.dev.loan_mgt_service.dto.request;

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
public class UpdateLoanRequest implements Serializable {
    private static final long serialVersionUID= 1L;

    @JsonProperty
    @Schema(description = "loan status")
    @NotBlank(message = "First name is required")
    @Pattern(regexp = "^(|PENDING|APPROVED|REJECTED|DISBURSED)$", message = "Status can only be either of PENDING, APPROVED, REJECTED, DISBURSED" )
    private String status;


}
