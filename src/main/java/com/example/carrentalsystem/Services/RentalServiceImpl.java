package com.example.carrentalsystem.Services;

import com.example.carrentalsystem.Models.Rental;
import com.example.carrentalsystem.Models.RentalStatus;
import com.example.carrentalsystem.Models.StatusHistory;
import com.example.carrentalsystem.Repositories.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service("rentalService")
public class RentalServiceImpl implements RentalService{
    private final RentalRepository rentalRepository;
    private final RentalStatusRepository rentalStatusRepository;
    private final StatusHistoryRepository statusHistoryRepository;

    public RentalServiceImpl(RentalRepository rentalRepository, RentalStatusRepository rentalStatusRepository, StatusHistoryRepository statusHistoryRepository) {
        this.rentalRepository = rentalRepository;
        this.rentalStatusRepository = rentalStatusRepository;
        this.statusHistoryRepository = statusHistoryRepository;
    }

    @Override
    public void changeRentalStatus(Long statusID, Long rentalID) {
        Rental rental = rentalRepository.getReferenceById(rentalID);
        RentalStatus rentalStatus = rentalStatusRepository.getReferenceById(statusID);
        rental.setRentalStatus(rentalStatus);

        StatusHistory newStatus = new StatusHistory(rentalStatus, LocalDate.now());
        rental.getStatusHistory().add(statusHistoryRepository.save(newStatus));
        rentalRepository.save(rental);
    }
}
