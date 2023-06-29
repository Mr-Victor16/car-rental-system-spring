package com.example.carrentalsystem.repositories;

import com.example.carrentalsystem.models.RentalStatusEnum;
import com.example.carrentalsystem.models.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByUserId(Long id);

    List<Rental> findByCarId(Long id);

    @Query("select (count(r) > 0) from Rental r where (r.startDate >= ?1 and r.endDate <= ?1) and r.rentalStatus.name = ?2")
    boolean existsByRentalDateAndRentalStatus(LocalDate startDate, RentalStatusEnum name);

    boolean existsByCarId(Long id);
}
