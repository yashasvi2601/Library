package com.MiniProject.Library_Management.security;

import com.MiniProject.Library_Management.exception.ResourceNotFoundException;
import com.MiniProject.Library_Management.model.Member;
import com.MiniProject.Library_Management.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

// A MEMBER may only touch their own records; LIBRARIAN/ADMIN may act on anyone's.
@Component
@RequiredArgsConstructor
public class MemberAccessGuard {

    private final MemberRepository memberRepository;

    public void verifyOwnerOrStaff(Long targetMemberId, Authentication authentication) {

        if (isStaff(authentication)) {
            return;
        }

        Member caller = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        if (!caller.getMemberId().equals(targetMemberId)) {
            throw new AccessDeniedException("You can only access your own records");
        }
    }

    public boolean isStaff(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_ADMIN") || a.equals("ROLE_LIBRARIAN"));
    }

    public Member currentMember(Authentication authentication) {
        return memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
    }
}
