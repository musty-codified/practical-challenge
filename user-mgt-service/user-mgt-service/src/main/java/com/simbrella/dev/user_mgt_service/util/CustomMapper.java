package com.simbrella.dev.user_mgt_service.util;

import com.simbrella.dev.user_mgt_service.dto.response.UserResponseDto;
import com.simbrella.dev.user_mgt_service.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
public class CustomMapper {

    public UserResponseDto mapToDto(User userEntity) {
        return UserResponseDto.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .phoneNumber(userEntity.getPhoneNumber())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .status(userEntity.getStatus().getValue())
                .build();
    }

}
