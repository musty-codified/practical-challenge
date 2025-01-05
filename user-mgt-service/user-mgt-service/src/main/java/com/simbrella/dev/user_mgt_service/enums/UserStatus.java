package com.simbrella.dev.user_mgt_service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    private final String value;

//    @JsonCreator
//    public static UserStatus create(String value) throws ValidationException {
//        if (value == null || value.isEmpty()){
//            return null;
//        }
//        for (UserStatus u : values()){
//            if(value.equalsIgnoreCase(u.getValue())){
//                return u;
//            }
//        }
//        return null;
//    }
}
