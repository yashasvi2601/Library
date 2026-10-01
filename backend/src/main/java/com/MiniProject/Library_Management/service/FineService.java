package com.MiniProject.Library_Management.service;

import com.MiniProject.Library_Management.dto.FineTableResponseDto;
import com.MiniProject.Library_Management.dto.PayFineRequestDto;
import com.MiniProject.Library_Management.exception.ResourceNotFoundException;
import com.MiniProject.Library_Management.model.*;
import com.MiniProject.Library_Management.repository.FineRepository;
import com.MiniProject.Library_Management.repository.ReservationRepository;
import com.MiniProject.Library_Management.security.MemberAccessGuard;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FineService {

    private final FineRepository fineRepository;
    private final ReservationRepository reservationRepository;
    private final MemberAccessGuard memberAccessGuard;

    @Transactional
    public FineTableResponseDto payFine(PayFineRequestDto dto, Authentication authentication) {

        Fine fine = fineRepository.findByFineIdAndPaidFalse(dto.getFineId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Unpaid fine not found with id: " + dto.getFineId()
                        ));

        //A member may only pay their own fine; staff may act on any
        memberAccessGuard.verifyOwnerOrStaff(fine.getMember().getMemberId(), authentication);

        // step 1 -> mark paid
        fine.setPaid(true);
        fine.setPaidAt(LocalDateTime.now());

        Transaction txn = fine.getTransaction();
        BookCopy copy = txn.getCopy();
        Book book = copy.getBook();

        // step 2 -> reservation queue handling
        List<Reservation> waitingReservations =
                reservationRepository.findByBookAndStatus(
                        book, ReservationStatus.WAITING
                );

        if (!waitingReservations.isEmpty()) {

            Reservation firstReservation = waitingReservations.get(0);

            firstReservation.setStatus(ReservationStatus.NOTIFIED);
            firstReservation.setNotifyBy(LocalDateTime.now());

            copy.setStatus(CopyStatus.RESERVED);
        }

        return mapFine(fineRepository.save(fine));
    }

    @Transactional(readOnly = true)
    public List<FineTableResponseDto> getMyPendingFines(Authentication authentication) {
        Member member = memberAccessGuard.currentMember(authentication);

        return fineRepository.findByMemberAndPaidFalse(member)
                .stream()
                .map(this::mapFine)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FineTableResponseDto> getPendingFines() {
        return fineRepository.findByPaidFalse()
                .stream()
                .map(this::mapFine)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FineTableResponseDto> getFineHistory() {
        return fineRepository.findByPaidTrue()
                .stream()
                .map(this::mapFine)
                .toList();
    }

    @Transactional
    public void deleteFine(Long fineId) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Fine not found"));

        fineRepository.delete(fine);
    }

    private FineTableResponseDto mapFine(Fine fine) {
        return FineTableResponseDto.builder()
                .fineId(fine.getFineId())
                .memberName(fine.getMember().getName())
                .email(fine.getMember().getEmail())
                .amount(fine.getAmount())
                .paidAt(fine.getPaidAt())
                .build();
    }
}