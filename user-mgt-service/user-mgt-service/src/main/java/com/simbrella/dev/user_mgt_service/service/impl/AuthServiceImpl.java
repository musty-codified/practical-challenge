package com.simbrella.dev.user_mgt_service.service.impl;

import com.simbrella.dev.user_mgt_service.config.security.CustomUserDetailsService;
import com.simbrella.dev.user_mgt_service.config.security.JwtUtils;
import com.simbrella.dev.user_mgt_service.dto.request.LoginRequestDto;
import com.simbrella.dev.user_mgt_service.dto.response.LoginResponseDto;
import com.simbrella.dev.user_mgt_service.entity.User;
import com.simbrella.dev.user_mgt_service.enums.UserStatus;
import com.simbrella.dev.user_mgt_service.exception.UnAuthorizedException;
import com.simbrella.dev.user_mgt_service.exception.UserNotFoundException;
import com.simbrella.dev.user_mgt_service.repository.UserRepository;
import com.simbrella.dev.user_mgt_service.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Value("${jwt.expiration}")
    private Long jwtExpiration;
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final JwtUtils jwtUtil;

    private final UserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public LoginResponseDto login(LoginRequestDto adminLoginRequestDto, HttpServletRequest request) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(adminLoginRequestDto.getEmail());
            if (userOptional.isEmpty()) {
                log.error("User not found with email {}", adminLoginRequestDto.getEmail());
                throw new UserNotFoundException("Invalid credentials", HttpStatus.UNAUTHORIZED.name());
            }

            User user = userOptional.get();
            if (user.getStatus().equals(UserStatus.INACTIVE)){
                throw new RuntimeException("User not active. Kindly activate your account");
            }

            if (!passwordEncoder.matches(adminLoginRequestDto.getPassword(), user.getPassword())){
                log.error("Bad credentials with email {}", adminLoginRequestDto.getEmail());
                throw new UserNotFoundException("Invalid credentials", HttpStatus.UNAUTHORIZED.name());
            }

            if (user.getRole() == null) {
                throw new IllegalArgumentException("Not assigned to any role; cannot log in");
            }
            log.info("Generating Access token for user {}", user.getEmail());
            final String accessToken = jwtUtil.generateToken(userDetailsService.loadUserByUsername(user.getEmail()));

            return LoginResponseDto.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .accessToken(accessToken)
                    .expiresIn(jwtExpiration)
                    .build();
        } catch (UserNotFoundException | AuthenticationException e) {
            throw new UnAuthorizedException("Incorrect credentials", HttpStatus.UNAUTHORIZED);
        }
    }

}
