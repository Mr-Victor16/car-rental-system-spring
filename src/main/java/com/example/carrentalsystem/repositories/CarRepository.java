package com.example.carrentalsystem.repositories;

import com.example.carrentalsystem.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByAvailable(boolean available);

    Car getCarById(Long carID);

    List<Car> findByMileage(Integer mileage);

    long countByModelName(String name);

    long countByBrandName(String name);

    boolean existsByBrandName(String name);

    boolean existsByModelName(String name);

}
