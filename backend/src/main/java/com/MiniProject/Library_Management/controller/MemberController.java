package com.MiniProject.Library_Management.controller;

import com.MiniProject.Library_Management.dto.MemberRequestDto;
import com.MiniProject.Library_Management.dto.MemberResponseDto;
import com.MiniProject.Library_Management.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public MemberResponseDto createMember(
            @RequestBody MemberRequestDto dto) {
        return memberService.createMember(dto);
    }

    @GetMapping("/{id}")
    public MemberResponseDto getMember(
            @PathVariable Long id) {
        return memberService.getMemberById(id);
    }

    @GetMapping
    public List<MemberResponseDto> getAllMembers() {
        return memberService.getAllMembers();
    }

    @PutMapping("/{id}")
    public MemberResponseDto updateMember(
            @PathVariable Long id,
            @RequestBody MemberRequestDto dto) {
        return memberService.updateMember(id, dto);
    }

    @DeleteMapping("/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return "Member deleted successfully";
    }
}