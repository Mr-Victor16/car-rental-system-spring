package com.example.carrentalsystem.Controllers;

import com.example.carrentalsystem.Models.*;
import com.example.carrentalsystem.Payload.Request.*;
import com.example.carrentalsystem.Repositories.*;
import org.springframework.http.HttpStatus;
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
    private final RentalRepository rentalRepository;

    public CarController(CarRepository carRepository, BrandRepository brandRepository, CarModelRepository carModelRepository,
                         FuelTypeRepository fuelTypeRepository, CarImageRepository carImageRepository, UserRepository userRepository,
                         RoleRepository roleRepository,
                         RentalRepository rentalRepository) {
        this.carRepository = carRepository;
        this.brandRepository = brandRepository;
        this.carModelRepository = carModelRepository;
        this.fuelTypeRepository = fuelTypeRepository;
        this.carImageRepository = carImageRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.rentalRepository = rentalRepository;
    }

    @GetMapping("available")
    public ResponseEntity<?> getAvailableCars(){
        return ResponseEntity.ok(carRepository.findByAvailable(true));
    }

    @GetMapping("all")
    public ResponseEntity<?> getAllCars(@RequestBody String token){
        if(userRepository.existsByToken(token)) {
            return ResponseEntity.ok(carRepository.findAll());
        } else {
            return new ResponseEntity<>("Bad token", HttpStatus.FORBIDDEN);
        }
    }

    @Transactional
    @PostMapping("add")
    public ResponseEntity<?> addCar(@RequestBody @Valid AddCarRequest carRequest){
        if(userRepository.getUserByToken(carRequest.getToken()).getRoles().contains(roleRepository.getByName(ERole.ROLE_ADMIN))){
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

            FuelType fuelType = fuelTypeRepository.findById(carRequest.getFuelType()).get();

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
                    carImageRepository.getByImageID(1L)
            ));

            return ResponseEntity.ok(carRepository.findByAvailable(true));
        } else {
            return new ResponseEntity<>("Bad token", HttpStatus.FORBIDDEN);
        }
    }

    @Transactional
    @PostMapping("change-image")
    public ResponseEntity<?> changeCarImage(@RequestParam("myFile") MultipartFile file,
                                            @RequestParam("carID") Long carID,
                                            @RequestParam("token") String token) throws IOException {

        if(userRepository.getUserByToken(token).getRoles().contains(roleRepository.getByName(ERole.ROLE_ADMIN))){
            Car car = carRepository.getCarById(carID);
            Long imageID = car.getCarImage().getImageID();

            CarImage carImage = carImageRepository.save(new CarImage(file.getBytes()));
            car.setCarImage(carImage);
            carImageRepository.deleteById(imageID);
            carRepository.save(car);

            return new ResponseEntity<>("Car photo changed", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Bad token", HttpStatus.FORBIDDEN);
        }
    }

    @Transactional
    @PostMapping("edit")
    public ResponseEntity<?> editCar(@RequestBody @Valid EditCarRequest carRequest){
        if(userRepository.getUserByToken(carRequest.getToken()).getRoles().contains(roleRepository.getByName(ERole.ROLE_ADMIN))){
            Car car = carRepository.getCarById(carRequest.getId());
            if(car != null){
                if(!carRequest.getBrand().equals(car.getBrand().getName())){
                    Brand brand = car.getBrand();
                    if(brandRepository.findByName(carRequest.getBrand()) == null){
                        car.setBrand(brandRepository.save(new Brand(carRequest.getBrand())));
                    } else {
                        car.setBrand(brandRepository.findByName(carRequest.getBrand()));
                    }

                    if(brandRepository.countByName(brand.getName()) == 1){
                        brandRepository.deleteByName(brand.getName());
                    }
                }

                if(!carRequest.getModel().equals(car.getModel().getName())){
                    CarModel carModel = car.getModel();

                    if(carModelRepository.findByName(carRequest.getModel()) == null){
                        car.setModel(carModelRepository.save(new CarModel(carRequest.getModel())));
                    } else {
                        car.setModel(carModelRepository.findByName(carRequest.getModel()));
                    }

                    if(carModelRepository.countByName(carModel.getName()) == 1){
                        carModelRepository.deleteByName(carModel.getName());
                    }
                }

                if(!carRequest.getCapacity().equals(car.getCapacity())){
                    car.setCapacity(carRequest.getCapacity());
                }

                if(!carRequest.getHorsePower().equals(car.getHorse_power())){
                    car.setHorse_power(carRequest.getHorsePower());
                }

                if(!carRequest.getYear().equals(car.getYear())){
                    car.setYear(carRequest.getYear());
                }

                if(!carRequest.getMileage().equals(car.getMileage())){
                    car.setMileage(carRequest.getMileage());
                }

                if(!carRequest.getPrice().equals(car.getPrice())){
                    car.setPrice(carRequest.getPrice());
                }

                if(!carRequest.getFuelType().equals(car.getFuelType().getId())){
                    car.setFuelType(fuelTypeRepository.findById(carRequest.getFuelType()).get());
                }

                carRepository.save(car);
                return new ResponseEntity<>("Car information changed", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Car not found", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Bad token", HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("get")
    public ResponseEntity<?> getCar(@RequestBody @Valid SimpleRequest request){
        if(userRepository.getUserByToken(request.getToken()).getRoles().contains(roleRepository.getByName(ERole.ROLE_ADMIN))){
            Car car = carRepository.getCarById(request.getId());
            if(car != null){
                return ResponseEntity.ok(car);
            } else {
                return new ResponseEntity<>("Car not found", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Bad token", HttpStatus.FORBIDDEN);
        }
    }

    @Transactional
    @DeleteMapping("delete")
    public ResponseEntity<?> deleteCar(@RequestBody @Valid SimpleRequest request){
        if(userRepository.getUserByToken(request.getToken()).getRoles().contains(roleRepository.getByName(ERole.ROLE_ADMIN))){
            Car car = carRepository.getCarById(request.getId());
            if(car != null){
                if(rentalRepository.findByCar_Id(car.getId()).size() == 0){
                    if(carRepository.findByModel_Name(car.getModel().getName()).size() == 1){
                        carModelRepository.deleteById(car.getModel().getId());
                    }

                    if(carRepository.findByBrand_Name(car.getBrand().getName()).size() == 1){
                        brandRepository.deleteById(car.getBrand().getId());
                    }

                    if(car.getCarImage().getImageID() != 1){
                        carImageRepository.deleteById(car.getCarImage().getImageID());
                    }

                    carRepository.deleteById(car.getId());
                    return new ResponseEntity<>("Car removed successfully", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("You cannot remove a car if it has a rental assigned to it", HttpStatus.CONFLICT);
                }
            } else {
                return new ResponseEntity<>("Car not found", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Bad token", HttpStatus.FORBIDDEN);
        }
    }

}
