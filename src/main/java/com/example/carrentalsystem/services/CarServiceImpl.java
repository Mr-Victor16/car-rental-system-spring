package com.example.carrentalsystem.services;

import com.example.carrentalsystem.models.*;
import com.example.carrentalsystem.payload.request.AddCarRequest;
import com.example.carrentalsystem.payload.request.EditCarRequest;
import com.example.carrentalsystem.repositories.BrandRepository;
import com.example.carrentalsystem.repositories.CarImageRepository;
import com.example.carrentalsystem.repositories.CarModelRepository;
import com.example.carrentalsystem.repositories.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service("carService")
public class CarServiceImpl implements CarService{
    private final CarRepository carRepository;
    private final BrandRepository brandRepository;
    private final CarModelRepository carModelRepository;
    private final CarImageRepository carImageRepository;
    private final FuelServiceImpl fuelService;

    public CarServiceImpl(CarRepository carRepository, BrandRepository brandRepository, CarModelRepository carModelRepository,
                          CarImageRepository carImageRepository, FuelServiceImpl fuelService) {
        this.carRepository = carRepository;
        this.brandRepository = brandRepository;
        this.carModelRepository = carModelRepository;
        this.carImageRepository = carImageRepository;
        this.fuelService = fuelService;
    }

    @Override
    public List<Car> findAvailableCars() {
        return carRepository.findByAvailable(true);
    }

    @Override
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    @Override
    public boolean existsById(Long carID) {
        return carRepository.existsById(carID);
    }

    @Override
    public Car getCarById(Long carID) {
        return carRepository.getCarById(carID);
    }

    public Brand addBrand(String brandName) {
        return Optional.ofNullable(brandRepository.findByName(brandName)).orElseGet(() -> brandRepository.save(new Brand(brandName)));
    }

    public CarModel addModel(String modelName) {
        return Optional.ofNullable(carModelRepository.findByName(modelName)).orElseGet(() -> carModelRepository.save(new CarModel(modelName)));
    }

    @Override
    @Transactional
    public void add(AddCarRequest carRequest) {
        Brand brand = addBrand(carRequest.getBrand());
        CarModel model = addModel(carRequest.getModel());

        FuelType fuelType = fuelService.findById(carRequest.getFuelType())
                .orElseThrow(() -> new RuntimeException("Error: Fuel type is not found."));

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
    }

    @Override
    public void changeImage(Long carID, MultipartFile file) throws IOException {
        Car car = carRepository.getCarById(carID);
        Long imageID = car.getCarImage().getImageID();

        CarImage carImage = carImageRepository.save(new CarImage(file.getBytes()));
        car.setCarImage(carImage);

        deleteImage(imageID);
        carRepository.save(car);
    }

    @Override
    @Transactional
    public void delete(Car car) {
        deleteModel(car.getModel());
        deleteBrand(car.getBrand());
        deleteImage(car.getCarImage().getImageID());

        carRepository.deleteById(car.getId());
    }

    @Override
    public void changeStatus(Car car) {
        car.setAvailable(!car.isAvailable());
        carRepository.save(car);
    }

    @Override
    @Transactional
    public void update(Long carID, EditCarRequest carRequest) {
        Car car = carRepository.getCarById(carID);

        if(!carRequest.getBrand().equals(car.getBrand().getName())){
            Brand brand = car.getBrand();
            car.setBrand(addBrand(carRequest.getBrand()));

            if(!carRepository.existsByBrandName(brand.getName())){
                brandRepository.deleteByName(brand.getName());
            }
        }

        if(!carRequest.getModel().equals(car.getModel().getName())){
            CarModel carModel = car.getModel();
            car.setModel(addModel(carRequest.getModel()));

            if(!carRepository.existsByModelName(carModel.getName())){
                carModelRepository.deleteByName(carModel.getName());
            }
        }

        if(!carRequest.getCapacity().equals(car.getCapacity())){
            car.setCapacity(carRequest.getCapacity());
        }

        if(!carRequest.getHorsePower().equals(car.getHorsePower())){
            car.setHorsePower(carRequest.getHorsePower());
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
            car.setFuelType(fuelService.findById(carRequest.getFuelType())
                    .orElseThrow(() -> new RuntimeException("Error: Fuel type is not found.")));
        }

        carRepository.save(car);
    }

    private void deleteImage(Long imageID){
        // CarImage with ID = 1 is the default image for new cars. For this reason, it must be protected from deletion
        if(imageID != 1) {
            carImageRepository.deleteById(imageID);
        }
    }

    private void deleteModel(CarModel model){
        if(carRepository.countByModelName(model.getName()) == 1){
            carModelRepository.deleteById(model.getId());
        }
    }

    private void deleteBrand(Brand brand){
        if(carRepository.countByBrandName(brand.getName()) == 1){
            brandRepository.deleteById(brand.getId());
        }
    }
}
