package com.MiniProject.Library_Management.repository;

import com.MiniProject.Library_Management.model.Book;
import com.MiniProject.Library_Management.model.Reservation;
import com.MiniProject.Library_Management.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByBookAndStatus(Book book, ReservationStatus status);
}