package com.example.carrentalsystem.Controllers;

import com.example.carrentalsystem.Models.*;
import com.example.carrentalsystem.Payload.Request.AddCarRequest;
import com.example.carrentalsystem.Payload.Response.MessageResponse;
import com.example.carrentalsystem.Repositories.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

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
    private final RoleRepository roleRepository;

    public CarController(CarRepository carRepository, BrandRepository brandRepository, CarModelRepository carModelRepository,
                         FuelTypeRepository fuelTypeRepository, CarImageRepository carImageRepository, UserRepository userRepository,
                         RoleRepository roleRepository) {
        this.carRepository = carRepository;
        this.brandRepository = brandRepository;
        this.carModelRepository = carModelRepository;
        this.fuelTypeRepository = fuelTypeRepository;
        this.carImageRepository = carImageRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Bad token!"));
        }
    }

    @Transactional
    @PostMapping("change-image")
    public ResponseEntity<?> changeCarImage(@Valid @RequestParam("myFile") MultipartFile file,
                                            @Valid @RequestParam("carID") Integer carID,
                                            @Valid @RequestParam("token") String token) throws IOException {

        if(userRepository.getByToken(token).getRoles().contains(roleRepository.getByName(ERole.ROLE_ADMIN))){
            Car car = carRepository.getCarById(carID);
            Integer imageID = car.getCarImage().getImageID();

            CarImage carImage = carImageRepository.save(new CarImage(file.getBytes()));
            car.setCarImage(carImage);
            carImageRepository.deleteById(imageID);
            carRepository.save(car);

            return ResponseEntity.ok(new MessageResponse("Success: Successfully changed car's image!"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Bad token!"));
        }
    }
}
