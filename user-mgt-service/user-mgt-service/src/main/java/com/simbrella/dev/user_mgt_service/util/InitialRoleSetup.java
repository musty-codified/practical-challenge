package com.simbrella.dev.user_mgt_service.util;
import com.simbrella.dev.user_mgt_service.entity.User;
import com.simbrella.dev.user_mgt_service.enums.UserStatus;
import com.simbrella.dev.user_mgt_service.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.simbrella.dev.user_mgt_service.enums.Roles.ROLE_ADMIN;

@AllArgsConstructor
@Component
@Slf4j
public class InitialRoleSetup {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @EventListener
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event){

        if (!userRepository.existsByEmail("musty@gmail.com")){
            System.out.println("Initializing Admin User");

            User admin = new User();
            admin.setStatus(UserStatus.ACTIVE);
            admin.setUpdatedAt(LocalDateTime.now());
            admin.setCreatedAt(LocalDateTime.now());
            admin.setPhoneNumber("8166099828");
            admin.setPassword(passwordEncoder.encode("0bv20S!ecQgdqd?o8bCWb~f>2J4Z(#}tuRARy12B>E9v]i=$OAsC"));
            admin.setEmail("musty@gmail.com");
            admin.setRole(ROLE_ADMIN.getPermissions().stream().map(Objects::toString).collect(Collectors.joining(",")));
            admin.setFirstName("Papi");
            admin.setLastName("Marciano");
            admin.setId(1L);
            userRepository.save(admin);
            System.out.println("Amin user created");

        } else {
            System.out.println("Amin user Already exists");
        }

    }




}
