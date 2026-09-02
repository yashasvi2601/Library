//package com.MiniProject.Library_Management.config;
//
//import com.MiniProject.Library_Management.security.JwtAuthFilter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.*;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.crypto.bcrypt.*;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final JwtAuthFilter jwtAuthFilter;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//                .cors(cors -> {})
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//
//                        // public endpoints
//                        .requestMatchers(
//                                "/api/auth/login",
//                                "/api/auth/signup"
//                        ).permitAll()
//
//                        // admin only
//                        .requestMatchers("/api/admin/**")
//                        .hasRole("ADMIN")
//
//                        // librarian + admin
//                        .requestMatchers(
//                                "/api/books/add",
//                                "/api/books/**",
//                                "/api/books/**",
//                                "/api/members/**"
//                        ).hasAnyRole("LIBRARIAN", "ADMIN")
//
//                        // member + librarian + admin
//                        .requestMatchers(
//                                "/api/books",
//                                "/api/books/search"
//                        ).hasAnyRole("MEMBER", "LIBRARIAN", "ADMIN")
//                        .requestMatchers("/api/circulation/member/**")
//                        .hasRole("MEMBER")
//                        .requestMatchers(
//                                "/api/auth/login",
//                                "/api/auth/signup"
//                        ).permitAll()
//                        .requestMatchers("/api/auth/logout")
//                        .authenticated()
//                        // circulation
//                        .requestMatchers(
//                                "/api/circulation/**",
//                                "/api/fines/**"
//                        ).hasAnyRole("MEMBER", "LIBRARIAN", "ADMIN")
//
//                        .anyRequest().authenticated()
//                )
//                .addFilterBefore(
//                        jwtAuthFilter,
//                        UsernamePasswordAuthenticationFilter.class
//                );
//
//        return http.build();
//    }
//}