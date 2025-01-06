package com.simbrella.dev.user_mgt_service.service.impl;

import com.simbrella.dev.user_mgt_service.dto.request.UpdateUserRequest;
import com.simbrella.dev.user_mgt_service.dto.request.UserRequestDto;
import com.simbrella.dev.user_mgt_service.dto.response.UserResponseDto;
import com.simbrella.dev.user_mgt_service.entity.User;
import com.simbrella.dev.user_mgt_service.enums.Roles;
import com.simbrella.dev.user_mgt_service.enums.UserStatus;
import com.simbrella.dev.user_mgt_service.exception.CustomValidationException;
import com.simbrella.dev.user_mgt_service.exception.UserAlreadyExistException;
import com.simbrella.dev.user_mgt_service.exception.UserNotFoundException;
import com.simbrella.dev.user_mgt_service.repository.UserRepository;
import com.simbrella.dev.user_mgt_service.service.UserService;
import com.simbrella.dev.user_mgt_service.util.AppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class  UserServiceImpl implements UserService {
    private final AppUtil appUtil;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Override
    public UserResponseDto createUser(UserRequestDto userDto) {

       validateEmail(userDto.getEmail());
       appUtil.validateEmailDomain(userDto.getEmail());
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserAlreadyExistException("User with email {" + userDto.getEmail() + "} already exists");
        }

        boolean isPhoneExist = isPhoneNumberExist(appUtil.getFormattedNumber(userDto.getPhoneNumber()));
        log.info("IsPhone exist:{}", isPhoneExist);
        if (isPhoneNumberExist(appUtil.getFormattedNumber(userDto.getPhoneNumber()))) {
            throw new UserAlreadyExistException("Phone number already exists");
        }

        User newUser = appUtil.getMapper().convertValue(userDto, User.class);
        newUser.setStatus(UserStatus.INACTIVE);
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setRole(Roles.ROLE_USER.getPermissions().stream().map(Objects::toString).collect(Collectors.joining(",")));

        newUser = userRepository.save(newUser);
        return appUtil.mapToDto(newUser);
    }

    @Override
    public UserResponseDto getUserDetails(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No user found with id: " + id, NOT_FOUND.name()));
        return appUtil.mapToDto(user);
    }

    @Override
    public UserResponseDto updateUser(long id, UpdateUserRequest userUpdate) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " does not exist", HttpStatus.NOT_FOUND.name()));

        if (userUpdate.getPhoneNumber() != null) {
            if (isPhoneNumberExist(appUtil.getFormattedNumber(userUpdate.getPhoneNumber()))) {
                throw new UserAlreadyExistException("Phone number already exists");
            }
            existingUser.setPhoneNumber(userUpdate.getPhoneNumber());
        }

        if (userUpdate.getFirstName() != null && !userUpdate.getFirstName().isEmpty()) {
            existingUser.setFirstName(userUpdate.getFirstName());
        }
        if (userUpdate.getLastName() != null) {
            existingUser.setLastName(userUpdate.getLastName());
        }

        if (userUpdate.getStatus() != null) {
            existingUser.setStatus(UserStatus.valueOf(userUpdate.getStatus()));
        }

        existingUser.setUpdatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(existingUser);
        return appUtil.mapToDto(savedUser);
    }

    @Override
    public void deleteUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No user found with id: " + id, NOT_FOUND.name()));
        userRepository.delete(user);
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
