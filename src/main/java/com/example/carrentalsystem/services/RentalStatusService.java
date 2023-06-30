package com.example.carrentalsystem.services;

import com.example.carrentalsystem.models.RentalStatus;
import com.example.carrentalsystem.models.RentalStatusEnum;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RentalStatusService {
    List<RentalStatus> findAll();

    boolean existsById(Long statusID);

    RentalStatus findByName(RentalStatusEnum rentalStatusEnum);

    RentalStatus getReferenceById(Long statusID);
}
