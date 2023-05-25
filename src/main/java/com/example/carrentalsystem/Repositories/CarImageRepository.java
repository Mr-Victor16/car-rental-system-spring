package com.example.carrentalsystem.Repositories;

import com.example.carrentalsystem.Models.CarImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarImageRepository extends JpaRepository<CarImage, Long> {
    CarImage getByImageID(Long imageID);
}
