package com.example.carrentalsystem.services;

import com.example.carrentalsystem.models.FuelType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface FuelService {
    List<FuelType> findAll();

    Optional<FuelType> findById(Long fuelType);
}
