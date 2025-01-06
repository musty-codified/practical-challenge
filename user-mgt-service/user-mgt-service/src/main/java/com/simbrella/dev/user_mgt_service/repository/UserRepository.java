package com.simbrella.dev.user_mgt_service.repository;

import com.simbrella.dev.user_mgt_service.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(@NotBlank(message = "Email is required") String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByEmail(String email);

}
