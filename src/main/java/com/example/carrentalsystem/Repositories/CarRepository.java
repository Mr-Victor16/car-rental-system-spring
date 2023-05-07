package com.example.carrentalsystem.Repositories;

import com.example.carrentalsystem.Models.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByAvailable(boolean available);

    Car getCarById(Integer carID);

    Car findByMileage(Integer mileage);
}
