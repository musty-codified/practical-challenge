package com.simbrella.dev.user_mgt_service.dto.response;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;

    @JsonIgnore
    private String roles;
    private String accessToken;
    private Long expiresIn;
}
