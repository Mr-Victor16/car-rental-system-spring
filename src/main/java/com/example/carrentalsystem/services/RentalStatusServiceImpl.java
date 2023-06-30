package com.example.carrentalsystem.services;

import com.example.carrentalsystem.models.RentalStatus;
import com.example.carrentalsystem.models.RentalStatusEnum;
import com.example.carrentalsystem.repositories.RentalStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("rentalStatusService")
public class RentalStatusServiceImpl implements RentalStatusService{
    private final RentalStatusRepository rentalStatusRepository;

    public RentalStatusServiceImpl(RentalStatusRepository rentalStatusRepository) {
        this.rentalStatusRepository = rentalStatusRepository;
    }

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
    public RentalStatus getReferenceById(Long statusID) {
        return rentalStatusRepository.getReferenceById(statusID);
    }
}
