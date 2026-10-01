package com.MiniProject.Library_Management.config;

import com.MiniProject.Library_Management.model.*;
import com.MiniProject.Library_Management.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Seeds sample books/members/loans for local development - runs once, only when the books table is empty.
@Component
@RequiredArgsConstructor
@Order(2)
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;
    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;
    private final FineRepository fineRepository;
    private final PasswordEncoder passwordEncoder;

    private record BookSeed(String isbn, String title, String author, String genre, int copies) {}
    private record MemberSeed(String name, String email, String password, Role role) {}

    @Override
    @Transactional
    public void run(String... args) {
        if (bookRepository.count() > 0) {
            return;
        }

        List<Book> books = seedBooks();
        List<Member> members = seedMembers();
        seedSampleLoans(books, members);

        log.info("Seeded {} books and {} members with sample circulation data.", books.size(), members.size());
    }

    private List<Book> seedBooks() {

        List<BookSeed> seeds = List.of(
                new BookSeed("978-0134685991", "Effective Java", "Joshua Bloch", "Programming", 4),
                new BookSeed("978-0596009205", "Head First Design Patterns", "Eric Freeman", "Programming", 3),
                new BookSeed("978-0132350884", "Clean Code", "Robert C. Martin", "Programming", 3),
                new BookSeed("978-0201633610", "Design Patterns", "Erich Gamma", "Programming", 2),
                new BookSeed("978-0439708180", "Harry Potter and the Sorcerer's Stone", "J. K. Rowling", "Fantasy", 5),
                new BookSeed("978-0547928227", "The Hobbit", "J. R. R. Tolkien", "Fantasy", 4),
                new BookSeed("978-0451524935", "1984", "George Orwell", "Fiction", 3),
                new BookSeed("978-0060850524", "Brave New World", "Aldous Huxley", "Fiction", 3),
                new BookSeed("978-0743273565", "The Great Gatsby", "F. Scott Fitzgerald", "Classics", 2),
                new BookSeed("978-0316769488", "The Catcher in the Rye", "J. D. Salinger", "Classics", 2),
                new BookSeed("978-0345391803", "The Hitchhiker's Guide to the Galaxy", "Douglas Adams", "Science Fiction", 3),
                new BookSeed("978-0385504201", "The Da Vinci Code", "Dan Brown", "Thriller", 3)
        );

        List<Book> saved = new ArrayList<>();

        for (BookSeed s : seeds) {
            Book book = Book.builder()
                    .isbn(s.isbn())
                    .title(s.title())
                    .author(s.author())
                    .genre(s.genre())
                    .totalCopies(s.copies())
                    .availableCopies(s.copies())
                    .build();

            book = bookRepository.save(book);

            for (int i = 0; i < s.copies(); i++) {
                BookCopy copy = BookCopy.builder()
                        .book(book)
                        .status(CopyStatus.AVAILABLE)
                        .condition(CopyCondition.GOOD)
                        .build();
                bookCopyRepository.save(copy);
            }

            saved.add(book);
        }

        return saved;
    }

    private List<Member> seedMembers() {

        List<MemberSeed> seeds = List.of(
                new MemberSeed("Lina Carter", "librarian@library.com", "librarian123", Role.LIBRARIAN),
                new MemberSeed("Arjun Mehta", "arjun.mehta@example.com", "member123", Role.MEMBER),
                new MemberSeed("Priya Nair", "priya.nair@example.com", "member123", Role.MEMBER),
                new MemberSeed("Sofia Rossi", "sofia.rossi@example.com", "member123", Role.MEMBER),
                new MemberSeed("Daniel Kim", "daniel.kim@example.com", "member123", Role.MEMBER),
                new MemberSeed("Omar Farouk", "omar.farouk@example.com", "member123", Role.MEMBER)
        );

        List<Member> saved = new ArrayList<>();

        for (MemberSeed s : seeds) {
            Member member = Member.builder()
                    .name(s.name())
                    .email(s.email())
                    .password(passwordEncoder.encode(s.password()))
                    .role(s.role())
                    .booksIssued(0)
                    .maxBooksAllowed(5)
                    .build();

            saved.add(memberRepository.save(member));
        }

        return saved;
    }

    private void seedSampleLoans(List<Book> books, List<Member> members) {
        // index 0 is the librarian account - only issue sample loans to members
        issueSampleLoan(books.get(0), members.get(1), 14);   // active loan, not due yet
        issueSampleLoan(books.get(4), members.get(2), 5);    // active loan, due soon
        issueSampleLoan(books.get(6), members.get(3), -5);   // overdue -> generates a pending fine
    }

    private void issueSampleLoan(Book book, Member member, int dueInDays) {

        BookCopy copy = bookCopyRepository.findByBookBookId(book.getBookId())
                .stream()
                .filter(c -> c.getStatus() == CopyStatus.AVAILABLE)
                .findFirst()
                .orElseThrow();

        copy.setStatus(CopyStatus.ISSUED);
        bookCopyRepository.save(copy);

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        member.setBooksIssued(member.getBooksIssued() + 1);
        memberRepository.save(member);

        LocalDate dueDate = LocalDate.now().plusDays(dueInDays);
        BigDecimal fineAmount = BigDecimal.ZERO;

        if (dueInDays < 0) {
            fineAmount = BigDecimal.valueOf(-dueInDays * 10L);
        }

        Transaction txn = Transaction.builder()
                .copy(copy)
                .member(member)
                .issuedAt(LocalDateTime.now().minusDays(14))
                .dueDate(dueDate)
                .fineAmount(fineAmount)
                .build();

        txn = transactionRepository.save(txn);

        if (dueInDays < 0) {
            Fine fine = Fine.builder()
                    .transaction(txn)
                    .member(member)
                    .amount(fineAmount)
                    .paid(false)
                    .build();

            fineRepository.save(fine);
        }
    }
}
