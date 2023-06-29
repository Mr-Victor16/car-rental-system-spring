package com.example.carrentalsystem.repositories;

import com.example.carrentalsystem.models.RentalStatusEnum;
import com.example.carrentalsystem.models.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalStatusRepository extends JpaRepository<RentalStatus, Long> {
    RentalStatus findByName(RentalStatusEnum name);

}