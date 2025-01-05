package com.simbrella.dev.user_mgt_service.repository;

import com.simbrella.dev.user_mgt_service.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(@NotBlank(message = "Email is required") String email);

    boolean existsByPhoneNumber(String phoneNumber);

}
