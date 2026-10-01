package com.MiniProject.Library_Management.service;

import com.MiniProject.Library_Management.dto.MemberRequestDto;
import com.MiniProject.Library_Management.dto.MemberResponseDto;
import com.MiniProject.Library_Management.exception.ResourceNotFoundException;
import com.MiniProject.Library_Management.model.Fine;
import com.MiniProject.Library_Management.model.Member;
import com.MiniProject.Library_Management.model.Role;
import com.MiniProject.Library_Management.repository.FineRepository;
import com.MiniProject.Library_Management.repository.MemberRepository;
import com.MiniProject.Library_Management.security.MemberAccessGuard;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final FineRepository fineRepository;
    private final MemberAccessGuard memberAccessGuard;

    public MemberResponseDto getMyProfile(Authentication authentication) {
        return mapToResponse(memberAccessGuard.currentMember(authentication));
    }

    // ==========================
    // CREATE MEMBER
    // ==========================
    @Transactional
    public MemberResponseDto createMember(MemberRequestDto dto, Authentication authentication) {

        Member member = Member.builder()
                .name(dto.getName())
                .email(dto.getEmail())

                // IMPORTANT CHANGE
                .password(passwordEncoder.encode(dto.getPassword()))

                .booksIssued(0)
                .maxBooksAllowed(
                        dto.getMaxBooksAllowed() != null
                                ? dto.getMaxBooksAllowed()
                                : 5
                )
                .role(resolveRole(dto.getRole(), authentication))
                .build();

        return mapToResponse(memberRepository.save(member));
    }

    public MemberResponseDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member not found with id: " + id
                        ));

        return mapToResponse(member);
    }

    public List<MemberResponseDto> getAllMembers() {
        return memberRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public MemberResponseDto updateMember(Long id, MemberRequestDto dto, Authentication authentication) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member not found with id: " + id
                        ));

        member.setName(dto.getName());
        member.setEmail(dto.getEmail());

        if (dto.getRole() != null) {
            member.setRole(resolveRole(dto.getRole(), authentication));
        }

        // optional password update
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            member.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getMaxBooksAllowed() != null) {
            if (dto.getMaxBooksAllowed() < member.getBooksIssued()) {
                throw new IllegalStateException(
                        "Max allowed books cannot be less than currently issued books"
                );
            }

            member.setMaxBooksAllowed(dto.getMaxBooksAllowed());
        }

        return mapToResponse(memberRepository.save(member));
    }

    @Transactional
    public void deleteMember(Long id) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member not found with id: " + id
                        ));

        if (member.getBooksIssued() > 0) {
            throw new IllegalStateException(
                    "Cannot delete member with issued books"
            );
        }

        memberRepository.delete(member);
    }

    //Only an admin may create or promote an account to LIBRARIAN/ADMIN
    private Role resolveRole(Role requestedRole, Authentication authentication) {

        if (requestedRole == null) {
            return Role.MEMBER;
        }

        if (requestedRole == Role.MEMBER) {
            return Role.MEMBER;
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new AccessDeniedException(
                    "Only an administrator can assign the " + requestedRole + " role");
        }

        return requestedRole;
    }

    private MemberResponseDto mapToResponse(Member member) {

        BigDecimal pendingFine = fineRepository.findByMemberAndPaidFalse(member)
                .stream()
                .map(Fine::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return MemberResponseDto.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .email(member.getEmail())
                .booksIssued(member.getBooksIssued())
                .maxBooksAllowed(member.getMaxBooksAllowed())
                .pendingFineAmount(pendingFine)
                .role(member.getRole())
                .build();
    }
}