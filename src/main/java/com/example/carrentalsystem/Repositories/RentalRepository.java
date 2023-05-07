package com.example.carrentalsystem.Repositories;

import com.example.carrentalsystem.Models.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByUser_Id(Long id);
}
