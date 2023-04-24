package com.example.carrentalsystem.Repositories;

import com.example.carrentalsystem.Models.ERentalStatus;
import com.example.carrentalsystem.Models.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalStatusRepository extends JpaRepository<RentalStatus, Long> {
    RentalStatus findByName(ERentalStatus name);

}