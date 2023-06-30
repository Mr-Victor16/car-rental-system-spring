package com.example.carrentalsystem.services;

import com.example.carrentalsystem.models.FuelType;
import com.example.carrentalsystem.repositories.FuelTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("fuelService")
public class FuelServiceImpl implements FuelService{
    private final FuelTypeRepository fuelTypeRepository;

    public FuelServiceImpl(FuelTypeRepository fuelTypeRepository) {
        this.fuelTypeRepository = fuelTypeRepository;
    }

    @Override
    public List<FuelType> findAll() {
        return fuelTypeRepository.findAll();
    }

    @Override
    public Optional<FuelType> findById(Long fuelType) {
        return fuelTypeRepository.findById(fuelType);
    }
}
