package com.example.carrentalsystem.services;

import org.springframework.stereotype.Service;

@Service
public interface RentalService {
    void changeRentalStatus(Long statusID, Long rentalID);
}
