package com.MiniProject.Library_Management.security;

import com.MiniProject.Library_Management.model.Member;
import com.MiniProject.Library_Management.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private static final String ADMIN_EMAIL = "admin@library.com";

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // hardcoded admin
        if (email.equalsIgnoreCase(ADMIN_EMAIL)) {
            return new User(
                    ADMIN_EMAIL,
                    "$2a$10$F9sL8vWRgNf/kgbuT10fnuzAf1HHuB2Vz/d4pY8EPp3rlxUloWsOe",
                    List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );
        }

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Member not found"));

        return new User(
                member.getEmail(),
                member.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_MEMBER"))
        );
    }
}

//package com.MiniProject.Library_Management.security;
//
//import com.MiniProject.Library_Management.model.Member;
//import com.MiniProject.Library_Management.repository.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.*;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final MemberRepository memberRepository;
//
//    private static final String ADMIN_EMAIL = "admin@library.com";
//
//    // bcrypt of admin123
//    private static final String ADMIN_PASSWORD =
//            "$2a$10$cwOIBmgosBIJJcRrJi8bRO7Noi/kns0lnA0UWRjvyPmdMzZ2rdOfy";
//
//    @Override
//    public UserDetails loadUserByUsername(String email)
//            throws UsernameNotFoundException {
//
//        // hardcoded admin
//        if (email.equals(ADMIN_EMAIL)) {
//            return new User(
//                    ADMIN_EMAIL,
//                    ADMIN_PASSWORD,
//                    List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
//            );
//        }
//
//        Member member = memberRepository.findByEmail(email)
//                .orElseThrow(() ->
//                        new UsernameNotFoundException("Member not found"));
//
//        return new User(
//                member.getEmail(),
//                member.getPassword(),
//                List.of(new SimpleGrantedAuthority("ROLE_MEMBER"))
//        );
//    }
//}