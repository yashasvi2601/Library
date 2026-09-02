package com.MiniProject.Library_Management.service;

import com.MiniProject.Library_Management.dto.BookRequestDTO;
import com.MiniProject.Library_Management.dto.BookResponseDTO;
import com.MiniProject.Library_Management.exception.ResourceNotFoundException;
import com.MiniProject.Library_Management.model.Book;
import com.MiniProject.Library_Management.model.BookCopy;
import com.MiniProject.Library_Management.model.CopyCondition;
import com.MiniProject.Library_Management.model.CopyStatus;
import com.MiniProject.Library_Management.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    //CREATE BOOK
    @Transactional
    public BookResponseDTO createBook(BookRequestDTO dto){

        Book book = Book.builder()
                .isbn(dto.getIsbn())
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .genre(dto.getGenre())
                .totalCopies(dto.getTotalCopies())
                .availableCopies(dto.getTotalCopies())
                .build();

        // auto create physical copies
        List<BookCopy> copies = new ArrayList<>();

        for (int i = 0; i < dto.getTotalCopies(); i++) {
            BookCopy copy = BookCopy.builder()
                    .book(book)
                    .status(CopyStatus.AVAILABLE)
                    .condition(CopyCondition.GOOD)
                    .build();

            copies.add(copy);
        }

        book.setCopies(copies);

        Book savedBook = bookRepository.save(book);

        return mapToResponse(savedBook);
    }

    //GET ALL BOOKS

    public List<BookResponseDTO> getAllBooks(){
        return bookRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    //GET BY ID

    public BookResponseDTO getBookById(Long id){
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        return mapToResponse(book);
    }

    //GET BY TITLE

    public List<BookResponseDTO> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(book -> BookResponseDTO.builder()
                        .bookId(book.getBookId())
                        .title(book.getTitle())
                        .isbn(book.getIsbn())
                        .genre(book.getGenre())
                        .totalCopies(book.getTotalCopies())
                        .availableCopies(book.getAvailableCopies())
                        .build())
                .toList();
    }

    //PUT BOOKS
    @Transactional
    public BookResponseDTO updateBook(Long id, BookRequestDTO dto) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book not found with id: " + id
                        ));

        int oldTotal = book.getTotalCopies();
        int newTotal = dto.getTotalCopies();

        // update metadata
        book.setIsbn(dto.getIsbn());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setGenre(dto.getGenre());

        List<BookCopy> copies = book.getCopies();

        // =============================
        // CASE 1: INCREASE COPIES
        // =============================
        if (newTotal > oldTotal) {

            int addCount = newTotal - oldTotal;

            for (int i = 0; i < addCount; i++) {
                copies.add(
                        BookCopy.builder()
                                .book(book)
                                .status(CopyStatus.AVAILABLE)
                                .condition(CopyCondition.GOOD)
                                .build()
                );
            }
        }

        // =============================
        // CASE 2: DECREASE COPIES
        // =============================
        else if (newTotal < oldTotal) {

            int removeCount = oldTotal - newTotal;

            List<BookCopy> removableCopies = copies.stream()
                    .filter(copy -> copy.getStatus() == CopyStatus.AVAILABLE)
                    .sorted((a, b) -> {
                        if (a.getCondition() == CopyCondition.DAMAGED &&
                                b.getCondition() == CopyCondition.GOOD) {
                            return -1;
                        }

                        if (a.getCondition() == CopyCondition.GOOD &&
                                b.getCondition() == CopyCondition.DAMAGED) {
                            return 1;
                        }

                        return 0;
                    })
                    .toList();

            if (removableCopies.size() < removeCount) {
                throw new IllegalStateException(
                        "Cannot reduce copies. Some copies are issued or reserved."
                );
            }

            for (int i = 0; i < removeCount; i++) {
                copies.remove(removableCopies.get(i));
            }
        }

        // sync total copies
        book.setTotalCopies(newTotal);

        // sync available copies
        long availableCount = copies.stream()
                .filter(copy -> copy.getStatus() == CopyStatus.AVAILABLE)
                .count();

        book.setAvailableCopies((int) availableCount);

        Book updatedBook = bookRepository.save(book);

        return mapToResponse(updatedBook);
    }

    //DELETE BOOKS
    @Transactional
    public void deleteBook(Long id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Book not found with id: " + id
                ));

        bookRepository.delete(book);
    }

    //HELPER

    private BookResponseDTO mapToResponse(Book book) {
        return BookResponseDTO.builder()
                .bookId(book.getBookId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .genre(book.getGenre())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getAvailableCopies())
                .build();
    }
}
