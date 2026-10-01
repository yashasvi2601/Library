package com.MiniProject.Library_Management.config;

import com.MiniProject.Library_Management.model.Member;
import com.MiniProject.Library_Management.model.Role;
import com.MiniProject.Library_Management.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Order(1)
public class AdminSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminSeeder.class);

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password:}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (memberRepository.findByEmail(adminEmail).isPresent()) {
            return;
        }

        String password = adminPassword;
        if (password == null || password.isBlank()) {
            password = UUID.randomUUID().toString();
            log.warn("app.admin.password (ADMIN_PASSWORD) is not set - generated a one-time admin password " +
                    "for '{}': {}. Log in and change it, or set ADMIN_PASSWORD before startup next time.",
                    adminEmail, password);
        }

        Member admin = Member.builder()
                .name("Administrator")
                .email(adminEmail)
                .password(passwordEncoder.encode(password))
                .role(Role.ADMIN)
                .booksIssued(0)
                .maxBooksAllowed(0)
                .build();

        memberRepository.save(admin);
        log.info("Seeded initial admin account '{}'.", adminEmail);
    }
}
