package com.example.carrentalsystem.repositories;

import com.example.carrentalsystem.models.CarImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarImageRepository extends JpaRepository<CarImage, Long> {
    CarImage getByImageID(Long imageID);
}
