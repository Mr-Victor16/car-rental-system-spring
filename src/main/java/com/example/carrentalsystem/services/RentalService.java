package com.example.carrentalsystem.services;

import com.example.carrentalsystem.models.Rental;
import com.example.carrentalsystem.models.RentalStatusEnum;
import com.example.carrentalsystem.payload.request.AddCarRentalRequest;
import com.example.carrentalsystem.payload.request.EditCarRentalRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface RentalService {
    void changeStatus(Long statusID, Long rentalID);

    List<Rental> findAll();

    List<Rental> findByUserId(Long userID);

    boolean existsById(Long rentalID);

    void update(Long rentalID, EditCarRentalRequest request);

    void delete(Long rentalID);

    Rental findById(Long rentalID);

    void add(AddCarRentalRequest request);

    boolean existsDateAndStatus(LocalDate now, RentalStatusEnum rentalStatusEnum);

    boolean existsByCarId(Long id);

}
