package com.MiniProject.Library_Management.controller;


import com.MiniProject.Library_Management.dto.BookRequestDTO;
import com.MiniProject.Library_Management.dto.BookResponseDTO;
import com.MiniProject.Library_Management.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public List<BookResponseDTO> getBooks(){

        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public BookResponseDTO getBooksById(@PathVariable Long id){

        return bookService.getBookById(id);
    }

    @PostMapping("/add")
    public BookResponseDTO addBook(@RequestBody BookRequestDTO bookRequestDTO){
        return bookService.createBook(bookRequestDTO);
    }

//    @GetMapping("/search")
//    public ResponseEntity<List<BookResponseDTO>> searchBooks(
//            @RequestParam String title) {
//        return ResponseEntity.ok(bookService.searchBooksByTitle(title));
//    }
    @GetMapping("/search")
    public List<BookResponseDTO> searchBooks(
            @RequestParam String title) {
        return bookService.searchBooksByTitle(title);
    }

    @PutMapping("/{id}")
    public BookResponseDTO updateBook(@PathVariable Long id, @RequestBody BookRequestDTO dto) {
        return bookService.updateBook(id, dto);
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "Book deleted successfully";
    }
}
