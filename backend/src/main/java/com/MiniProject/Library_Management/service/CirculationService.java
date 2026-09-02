package com.MiniProject.Library_Management.service;

import com.MiniProject.Library_Management.dto.*;
import com.MiniProject.Library_Management.exception.ResourceNotFoundException;
import com.MiniProject.Library_Management.model.*;
import com.MiniProject.Library_Management.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CirculationService {

    private final BookRepository bookRepository;
    private final BookCopyRepository copyRepository;
    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;
    private final FineRepository fineRepository;
    private final ReservationRepository reservationRepository;

    // =========================
    // CHECKOUT
    // =========================
    @Transactional
    public TransactionResponseDto checkoutBook(CheckoutRequestDto dto) {

        //Check if Book exists or not
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        //Check if member exsits or not
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        //Check Book issue limit by a member
        if (member.getBooksIssued() >= member.getMaxBooksAllowed()) {
            throw new IllegalStateException("Member issue limit exceeded");
        }

        //Check if book_copy is available or not
        BookCopy copy = book.getCopies().stream()
                .filter(c -> c.getStatus() == CopyStatus.AVAILABLE)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException("No available copy"));
        //Reservation is checked here.
        List<Reservation> reservations =
                reservationRepository.findByBookAndStatus(
                        book, ReservationStatus.WAITING);
        //Only first reserved member can issue
        if (!reservations.isEmpty() &&
                !reservations.get(0).getMember().getMemberId()
                        .equals(member.getMemberId())) {
            throw new IllegalStateException(
                    "Book reserved for another member");
        }

        //Update copy status to ISSUED
        copy.setStatus(CopyStatus.ISSUED);

        //Increment member active issued count
        member.setBooksIssued(member.getBooksIssued() + 1);

        // Update book available copy cache
        book.setAvailableCopies(book.getAvailableCopies() - 1);

        //Create transaction row
        Transaction txn = Transaction.builder()
                .copy(copy)
                .member(member)
                .issuedAt(LocalDateTime.now())
                .dueDate(LocalDate.now().plusDays(14))
                .fineAmount(BigDecimal.ZERO)
                .build();

        //Save transaction and return transactionDto
        Transaction savedTxn = transactionRepository.save(txn);

        return mapToResponse(savedTxn, "Book checked out successfully");
    }

    // =========================
    // RETURN
    // =========================
    @Transactional
    public TransactionResponseDto returnBook(ReturnRequestDto dto) {

        //Validate transaction
        Transaction txn = transactionRepository.findById(dto.getTxnId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Transaction not found"));

        //Validate transaction
        if (txn.getReturnedAt() != null) {
            throw new IllegalStateException("Book already returned");
        }

        LocalDate today = LocalDate.now();

        //Check overdue and calculate fine
        if (today.isAfter(txn.getDueDate())) {
            long overdueDays =
                    ChronoUnit.DAYS.between(txn.getDueDate(), today);

            BigDecimal fine =
                    BigDecimal.valueOf(overdueDays * 10);

            //Update transaction fine amount
            txn.setFineAmount(fine);

            //Create fine row
            Fine fineRow = Fine.builder()
                    .transaction(txn)
                    .member(txn.getMember())
                    .amount(fine)
                    .paid(false)
                    .build();

            fineRepository.save(fineRow);
        }

        //Mark transaction as returned
        txn.setReturnedAt(LocalDateTime.now());

        //Update copy status back to AVAILABLE
        BookCopy copy = txn.getCopy();
        copy.setStatus(CopyStatus.AVAILABLE);

        //Increment book available cache
        Book book = copy.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);

        //Decrement member issued count
        Member member = txn.getMember();
        member.setBooksIssued(member.getBooksIssued() - 1);

        //Save updated transaction
        Transaction updatedTxn = transactionRepository.save(txn);

        return mapToResponse(
                updatedTxn,
                "Book returned successfully"
        );
    }

    // =========================
    // RENEW
    // =========================
    @Transactional
    public TransactionResponseDto renewBook(RenewRequestDto dto) {

        //Validate transaction
        Transaction txn = transactionRepository.findById(dto.getTxnId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Transaction not found"));

        //Prevent renewal after return
        if (txn.getReturnedAt() != null) {
            throw new IllegalStateException("Returned books cannot renew");
        }

        LocalDate today = LocalDate.now();

        //If overdue, create fine
        if (today.isAfter(txn.getDueDate())) {
            long overdueDays =
                    ChronoUnit.DAYS.between(txn.getDueDate(), today);

            BigDecimal fine =
                    BigDecimal.valueOf(overdueDays * 10);

            Fine fineRow = Fine.builder()
                    .transaction(txn)
                    .member(txn.getMember())
                    .amount(fine)
                    .paid(false)
                    .build();

            fineRepository.save(fineRow);
        }

        //Check reservation queue before renew
        Book book = txn.getCopy().getBook();

        List<Reservation> reservations =
                reservationRepository.findByBookAndStatus(
                        book, ReservationStatus.WAITING);

        if (!reservations.isEmpty()) {
            throw new IllegalStateException(
                    "Cannot renew, reservation queue exists");
        }

        //Extend issue period
        txn.setIssuedAt(LocalDateTime.now());
        txn.setDueDate(LocalDate.now().plusDays(14));

        //Save renewed transaction
        Transaction renewedTxn = transactionRepository.save(txn);

        return mapToResponse(
                renewedTxn,
                "Book renewed successfully"
        );
    }

    @Transactional
    public List<MemberIssuedBookDto> getBooksForMember(Long memberId) {
        return transactionRepository.findByMemberMemberIdAndReturnedAtIsNull(memberId)
                .stream()
                .map(txn -> {
                    long daysLeft =
                            ChronoUnit.DAYS.between(LocalDate.now(), txn.getDueDate());

                    BigDecimal fine = BigDecimal.ZERO;

                    if (LocalDate.now().isAfter(txn.getDueDate())) {
                        long overdueDays =
                                ChronoUnit.DAYS.between(txn.getDueDate(), LocalDate.now());

                        fine = BigDecimal.valueOf(overdueDays * 10);
                    }

                    return MemberIssuedBookDto.builder()
                            .txnId(txn.getTxnId())
                            .bookId(txn.getCopy().getBook().getBookId())
                            .title(txn.getCopy().getBook().getTitle())
                            .issuedAt(txn.getIssuedAt())
                            .dueDate(txn.getDueDate())
                            .daysLeft(daysLeft)
                            .currentFine(fine)
                            .build();
                })
                .toList();
    }

    @Transactional
    public List<IssuedBookResponseDto> getIssuedBooks() {
        return transactionRepository.findAll()
                .stream()
                .map(txn -> IssuedBookResponseDto.builder()
                        .memberName(txn.getMember().getName())
                        .email(txn.getMember().getEmail())
                        .bookTitle(txn.getCopy().getBook().getTitle())
                        .copyId(txn.getCopy().getCopyId())
                        .status(txn.getCopy().getStatus().name())
                        .build())
                .toList();
    }

    private TransactionResponseDto mapToResponse(Transaction txn, String message) {

        return TransactionResponseDto.builder()
                .txnId(txn.getTxnId())
                .copyId(txn.getCopy().getCopyId())
                .memberId(txn.getMember().getMemberId())
                .issuedAt(txn.getIssuedAt())
                .dueDate(txn.getDueDate())
                .returnedAt(txn.getReturnedAt())
                .fineAmount(txn.getFineAmount())
                .message(message)
                .build();
    }
}