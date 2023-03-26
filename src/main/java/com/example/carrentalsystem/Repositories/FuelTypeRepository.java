package com.example.carrentalsystem.Repositories;

import com.example.carrentalsystem.Models.EFuelType;
import com.example.carrentalsystem.Models.FuelType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuelTypeRepository extends JpaRepository<FuelType, Long> {
    FuelType findByName(EFuelType name);

    FuelType getById(Integer id);
}
