package com.MiniProject.Library_Management.repository;

import com.MiniProject.Library_Management.model.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookCopyRepository extends JpaRepository<BookCopy,Long> {
    List<BookCopy> findByBookBookId(Long bookId);
}
