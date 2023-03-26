package com.example.carrentalsystem.Controllers;

import com.example.carrentalsystem.Models.*;
import com.example.carrentalsystem.Payload.Request.AddCarRequest;
import com.example.carrentalsystem.Repositories.*;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cars")
public class CarController {
    private final CarRepository carRepository;
    private final BrandRepository brandRepository;
    private final CarModelRepository carModelRepository;
    private final FuelTypeRepository fuelTypeRepository;
    private final CarImageRepository carImageRepository;
    private final UserRepository userRepository;

    public CarController(CarRepository carRepository, BrandRepository brandRepository, CarModelRepository carModelRepository,
                         FuelTypeRepository fuelTypeRepository, CarImageRepository carImageRepository, UserRepository userRepository) {
        this.carRepository = carRepository;
        this.brandRepository = brandRepository;
        this.carModelRepository = carModelRepository;
        this.fuelTypeRepository = fuelTypeRepository;
        this.carImageRepository = carImageRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("available")
    public ResponseEntity<?> getAvailableCars(){
        return ResponseEntity.ok(carRepository.findByAvailable(true));
    }

    @Transactional
    @PostMapping("add")
    public ResponseEntity<?> addCar(@Valid @RequestBody AddCarRequest carRequest){
        if(userRepository.getByToken(carRequest.getToken()).getRoles().contains(ERole.ROLE_ADMIN)){
            Brand brand;
            if (brandRepository.findByName(carRequest.getBrand()) != null) {
                brand = brandRepository.findByName(carRequest.getBrand());
            } else {
                brand = brandRepository.save(new Brand(carRequest.getBrand()));
            }

            CarModel model;
            if (carModelRepository.findByName(carRequest.getModel()) != null) {
                model = carModelRepository.findByName(carRequest.getModel());
            } else {
                model = carModelRepository.save(new CarModel(carRequest.getModel()));
            }

            FuelType fuelType = fuelTypeRepository.getById(carRequest.getFuelType());

            carRepository.save(new Car(
                    brand,
                    model,
                    carRequest.getYear(),
                    carRequest.getMileage(),
                    fuelType,
                    carRequest.getHorsePower(),
                    carRequest.getCapacity(),
                    carRequest.getPrice(),
                    true,
                    carImageRepository.getByImageID(1)
            ));

            return ResponseEntity.ok(carRepository.findByAvailable(true));
        } else {
            return (ResponseEntity<?>) ResponseEntity.badRequest();
        }
    }

    public static byte[] imageFromURLToByteArray(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        conn.connect();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(conn.getInputStream(), baos);

        return baos.toByteArray();
    }
}
