package com.example.carrentalsystem.repositories;

import com.example.carrentalsystem.models.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarModelRepository extends JpaRepository<CarModel, Long> {
    CarModel findByName(String name);

    void deleteByName(String name);

}
