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
        log.info("Initializing roles and permissions...");

        if (!userRepository.existsByEmail("musty@gmail.com")){
            System.out.println("Initializing Admin User");

            User superAdminUser = new User();
            superAdminUser.setStatus(UserStatus.ACTIVE);
            superAdminUser.setUpdatedAt(LocalDateTime.now());
            superAdminUser.setCreatedAt(LocalDateTime.now());
            superAdminUser.setPhoneNumber("8166099828");
            superAdminUser.setPassword(passwordEncoder.encode("0bv20S!ecQgdqd?o8bCWb~f>2J4Z(#}tuRARy12B>E9v]i=$OAsC"));
            superAdminUser.setEmail("musty@gmail.com");
            superAdminUser.setRole(ROLE_ADMIN.getPermissions().stream().map(Objects::toString).collect(Collectors.joining(",")));
            superAdminUser.setFirstName("Papi");
            superAdminUser.setLastName("Marciano");
            superAdminUser.setId(1L);
            userRepository.save(superAdminUser);
            System.out.println("Amin user created");

        } else {
            System.out.println("Amin user Already exists");
        }

    }




}
