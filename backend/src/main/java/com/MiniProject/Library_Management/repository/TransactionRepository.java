package com.MiniProject.Library_Management.repository;

import com.MiniProject.Library_Management.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    long countByReturnedAtIsNull();
    List<Transaction> findByMemberMemberIdAndReturnedAtIsNull(Long memberId);
}