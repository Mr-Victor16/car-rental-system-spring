package com.example.carrentalsystem.services;

import com.example.carrentalsystem.models.RentalStatus;
import com.example.carrentalsystem.models.RentalStatusEnum;
import com.example.carrentalsystem.repositories.RentalStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("rentalStatusService")
@RequiredArgsConstructor
public class RentalStatusServiceImpl implements RentalStatusService{
    private final RentalStatusRepository rentalStatusRepository;

    @Override
    public List<RentalStatus> findAll() {
        return rentalStatusRepository.findAll();
    }

    @Override
    public boolean existsById(Long statusID) {
        return rentalStatusRepository.existsById(statusID);
    }

    @Override
    public RentalStatus findByName(RentalStatusEnum rentalStatusEnum) {
        return rentalStatusRepository.findByName(rentalStatusEnum);
    }

    @Override
    public Optional<RentalStatus> findById(Long rentalStatus) {
        return rentalStatusRepository.findById(rentalStatus);
    }
}
