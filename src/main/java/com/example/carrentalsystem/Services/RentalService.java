package com.example.carrentalsystem.Services;

import org.springframework.stereotype.Service;

@Service
public interface RentalService {
    void changeRentalStatus(Long statusID, Long rentalID);
}
