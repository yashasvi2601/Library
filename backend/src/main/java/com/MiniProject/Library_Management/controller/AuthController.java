package com.MiniProject.Library_Management.controller;

import com.MiniProject.Library_Management.dto.LoginRequest;
import com.MiniProject.Library_Management.dto.LoginResponse;
import com.MiniProject.Library_Management.dto.SignupRequest;
import com.MiniProject.Library_Management.repository.MemberRepository;
import com.MiniProject.Library_Management.security.JwtService;
import com.MiniProject.Library_Management.service.AuthService;
import com.MiniProject.Library_Management.service.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthService authService;
    private final LogoutService logoutService;
    private  final MemberRepository memberRepository;

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest request) {
        return authService.signup(request);
    }
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        String email = request.getEmail().toLowerCase().trim();

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        request.getPassword()
                )
        );

        String role = auth.getAuthorities()
                .iterator()
                .next()
                .getAuthority()
                .replace("ROLE_", "");

        String token = jwtService.generateToken(email, role);

        Long memberId = null;

        if (role.equals("MEMBER")) {
            memberId = memberRepository.findByEmail(email)
                    .orElseThrow()
                    .getMemberId();
        }

        return new LoginResponse(
                token,
                role,
                email,
                memberId
        );
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        logoutService.logout(token);
        return "Logged out successfully";
    }
}