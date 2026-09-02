package com.MiniProject.Library_Management.repository;

import com.MiniProject.Library_Management.model.Fine;
import com.MiniProject.Library_Management.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FineRepository extends JpaRepository<Fine, Long> {
    Optional<Fine> findByFineIdAndPaidFalse(Long fineId);
    List<Fine> findByPaidFalse();
    List<Fine> findByPaidTrue();
    List<Fine> findByMemberAndPaidFalse(Member member);
}