package com.example.carrentalsystem.services;

import com.example.carrentalsystem.models.RentalStatus;
import com.example.carrentalsystem.models.RentalStatusEnum;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface RentalStatusService {
    List<RentalStatus> findAll();

    boolean existsById(Long statusID);

    RentalStatus findByName(RentalStatusEnum rentalStatusEnum);

    Optional<RentalStatus> findById(Long rentalStatus);
}
