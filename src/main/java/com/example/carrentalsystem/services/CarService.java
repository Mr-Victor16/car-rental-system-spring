package com.example.carrentalsystem.services;

import com.example.carrentalsystem.models.Car;
import com.example.carrentalsystem.payload.request.AddCarRequest;
import com.example.carrentalsystem.payload.request.EditCarRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface CarService {
    List<Car> findAvailableCars();

    List<Car> findAll();

    boolean existsById(Long carID);

    Car getCarById(Long carID);

    void add(AddCarRequest carRequest);

    void changeImage(Long carID, MultipartFile file) throws IOException;

    void delete(Car car);

    void changeStatus(Car car);

    void update(Long carID, EditCarRequest carRequest);
}
