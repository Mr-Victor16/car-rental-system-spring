package com.example.carrentalsystem.Repositories;

import com.example.carrentalsystem.Models.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {

}
