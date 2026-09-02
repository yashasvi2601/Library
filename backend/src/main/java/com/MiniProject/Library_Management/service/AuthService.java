package com.MiniProject.Library_Management.service;

import com.MiniProject.Library_Management.dto.SignupRequest;
import com.MiniProject.Library_Management.model.Member;
import com.MiniProject.Library_Management.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public String signup(SignupRequest request) {

        if (memberRepository.existsByEmail(request.getEmail().toLowerCase().trim())) {
            throw new RuntimeException("Email already registered");
        }

        Member member = Member.builder()
                .name(request.getName())
                .email(request.getEmail().toLowerCase().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .booksIssued(0)
                .maxBooksAllowed(5)
                .build();

        memberRepository.save(member);

        return "Member registered successfully";
    }
}