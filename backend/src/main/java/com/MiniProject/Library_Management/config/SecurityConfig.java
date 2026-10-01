package com.MiniProject.Library_Management.config;

import com.MiniProject.Library_Management.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // CORS preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // public endpoints
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/signup"
                        ).permitAll()
                        .requestMatchers("/api/auth/logout").authenticated()

                        // admin only
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // books: anyone authenticated can browse, only staff can manage
                        .requestMatchers(HttpMethod.GET, "/api/books/**")
                        .hasAnyRole("MEMBER", "LIBRARIAN", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/books/**")
                        .hasAnyRole("LIBRARIAN", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/books/**")
                        .hasAnyRole("LIBRARIAN", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/books/**")
                        .hasAnyRole("LIBRARIAN", "ADMIN")

                        // a member may view/manage their own profile; staff manage all members
                        .requestMatchers("/api/members/me")
                        .hasAnyRole("MEMBER", "LIBRARIAN", "ADMIN")
                        .requestMatchers("/api/members/**")
                        .hasAnyRole("LIBRARIAN", "ADMIN")

                        // circulation desk operations: librarian + admin only
                        .requestMatchers("/api/circulation/issued")
                        .hasAnyRole("LIBRARIAN", "ADMIN")

                        // self-service circulation: member (ownership enforced in code) + staff
                        .requestMatchers(
                                "/api/circulation/checkout",
                                "/api/circulation/return",
                                "/api/circulation/renew",
                                "/api/circulation/member/**"
                        ).hasAnyRole("MEMBER", "LIBRARIAN", "ADMIN")

                        // a member may view/pay their own fines; staff manage all fines
                        .requestMatchers("/api/fines/my", "/api/fines/pay")
                        .hasAnyRole("MEMBER", "LIBRARIAN", "ADMIN")
                        .requestMatchers("/api/fines/**")
                        .hasAnyRole("LIBRARIAN", "ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
