package com.simbrella.dev.user_mgt_service.service.impl;

import com.simbrella.dev.user_mgt_service.dto.request.UserRequestDto;
import com.simbrella.dev.user_mgt_service.dto.response.UserResponseDto;
import com.simbrella.dev.user_mgt_service.entity.User;
import com.simbrella.dev.user_mgt_service.enums.Roles;
import com.simbrella.dev.user_mgt_service.enums.UserStatus;
import com.simbrella.dev.user_mgt_service.exception.CustomValidationException;
import com.simbrella.dev.user_mgt_service.exception.UserAlreadyExistException;
import com.simbrella.dev.user_mgt_service.repository.UserRepository;
import com.simbrella.dev.user_mgt_service.service.UserService;
import com.simbrella.dev.user_mgt_service.util.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AppUtil appUtil;
    private final UserRepository userRepository;
    @Override
    public UserResponseDto createUser(UserRequestDto userDto) {

       validateEmail(userDto.getEmail());
       appUtil.validateEmailDomain(userDto.getEmail());
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserAlreadyExistException("User with email {" + userDto.getEmail() + "} already exists");
        }
        if (isPhoneNumberExist(appUtil.getFormattedNumber(userDto.getPhoneNumber()))) {
            throw new UserAlreadyExistException("Phone number already exists");
        }

        User newUser = appUtil.getMapper().convertValue(userDto, User.class);
        newUser.setStatus(UserStatus.INACTIVE);
        newUser.setPassword("######passsword");
        newUser.setRole(Roles.ROLE_USER.getPermissions().stream().map(Objects::toString).collect(Collectors.joining(",")));

        newUser = userRepository.save(newUser);
        return appUtil.mapToDto(newUser);
    }


    private void validateEmail(String email) {
        if (!AppUtil.isEmailValid(email)) {
            throw new CustomValidationException("Invalid email format {" + email + "}");
        }
    }
    private boolean isPhoneNumberExist(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }
}
