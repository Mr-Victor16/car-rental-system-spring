package com.example.carrentalsystem.Repositories;

import com.example.carrentalsystem.Models.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarModelRepository extends JpaRepository<CarModel, Long> {
    CarModel findByName(String name);

    void deleteByName(String name);

    int countByName(String name);
}
