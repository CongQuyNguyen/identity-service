package com.congquynguyen.identityservice.configuration;

import com.congquynguyen.identityservice.entity.UserEntity;
import com.congquynguyen.identityservice.enums.Role;
import com.congquynguyen.identityservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Slf4j
@Configuration
public class ApplicationInitConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if(userRepository.findByUsername("admin").isEmpty()) {
                HashSet<String> roles = new HashSet<>();
                roles.add(Role.ADMIN.name());
                UserEntity admin = UserEntity.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();
                userRepository.save(admin);
                log.warn("ADMIN has been created");
            }
        };
    }
}
