package com.example.carrentalsystem.services;

import com.example.carrentalsystem.models.*;
import com.example.carrentalsystem.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CarServiceTests {
    private CarRepository carRepository;
    private BrandRepository brandRepository;
    private CarModelRepository carModelRepository;
    private CarImageRepository carImageRepository;
    private FuelServiceImpl fuelTypeService;
    private FuelTypeRepository fuelTypeRepository;
    private CarService carService;

    @BeforeEach
    void setUp(){
        carRepository = mock(CarRepository.class);
        brandRepository = mock(BrandRepository.class);
        carModelRepository = mock(CarModelRepository.class);
        carImageRepository = mock(CarImageRepository.class);
        fuelTypeRepository = mock(FuelTypeRepository.class);
        fuelTypeService = new FuelServiceImpl(fuelTypeRepository);
        carService = new CarServiceImpl(carRepository, brandRepository, carModelRepository, carImageRepository, fuelTypeService);
    }

    //boolean existsById(Long carID);
    //Test when car exists in the database
    @Test
    void existsByIdWhenCarExists(){
        Long carID = 1L;
        when(carRepository.existsById(carID)).thenReturn(true);

        boolean result = carService.existsById(carID);
        assertTrue(result);
    }

    //boolean existsById(Long carID);
    //Test when car doesn't exist in the database
    @Test
    void existsByIdWhenCarDoesNotExist(){
        Long carID = 2L;
        when(carRepository.existsById(carID)).thenReturn(false);

        boolean result = carService.existsById(carID);
        assertFalse(result);
    }

    //boolean existsById(Long carID);
    //Test when car ID is invalid
    @Test
    void existsByIdWhenCarIdIsInvalid(){
        boolean result = carService.existsById(null);
        assertFalse(result);
    }

    //List<Car> findAvailableCars();
    //Test when cars have different availability status
    @Test
    void findAvailableCarsWithMixedAvailability(){
        Brand brand = new Brand("BrandName");
        Brand brand1 = new Brand("BrandName1");

        CarModel model = new CarModel("ModelName");
        CarModel model1 = new CarModel("ModelName1");
        CarModel model2 = new CarModel("ModelName2");

        CarImage carPhoto = new CarImage(1L, "fileContent".getBytes());

        FuelType diesel = new FuelType(FuelTypeEnum.FUEL_DIESEL);
        FuelType gasoline = new FuelType(FuelTypeEnum.FUEL_GASOLINE);

        Car availableCar = new Car(brand, model, 1998, 350000, diesel, 110, "1.9 TDI", 200, true, carPhoto);
        Car unavailableCar = new Car(brand1, model1, 2009, 198000, gasoline, 70, "1.4 TDI", 300, false, carPhoto);
        Car availableCar1 = new Car(brand, model2, 2003, 150000, diesel, 90, "1.9 TDI", 150, true, carPhoto);

        List<Car> availableCars = Arrays.asList(availableCar, availableCar1);

        when(carRepository.findByAvailable(true)).thenReturn(availableCars);

        List<Car> result = carService.findAvailableCars();

        assertEquals(Arrays.asList(availableCar, availableCar1), result);
    }

    //List<Car> findAvailableCars();
    //Test when no cars with available status
    @Test
    void findAvailableCarsWhenNoCarsAvailable(){
        Brand brand = new Brand("BrandName");
        Brand brand1 = new Brand("BrandName1");

        CarModel model = new CarModel("ModelName");
        CarModel model1 = new CarModel("ModelName1");

        CarImage carPhoto = new CarImage(1L, "fileContent".getBytes());

        FuelType diesel = new FuelType(FuelTypeEnum.FUEL_DIESEL);
        FuelType gasoline = new FuelType(FuelTypeEnum.FUEL_GASOLINE);

        Car unavailableCar = new Car(brand, model, 1998, 350000, diesel, 110, "1.9 TDI", 200, false, carPhoto);
        Car unavailableCar1 = new Car(brand1, model1, 2009, 198000, gasoline, 70, "1.4 TDI", 300, false, carPhoto);

        List<Car> expectedCars = Collections.emptyList();

        when(carRepository.findByAvailable(true)).thenReturn(expectedCars);

        List<Car> result = carService.findAvailableCars();

        assertEquals(expectedCars.size(), result.size());
    }

    //List<Car> findAvailableCars();
    //Test when no cars in the database
    @Test
    void findAvailableCarsWhenNoCarsInDatabase(){
        List<Car> expectedCars = Collections.emptyList();

        when(carRepository.findByAvailable(true)).thenReturn(expectedCars);

        List<Car> result = carService.findAvailableCars();

        assertEquals(expectedCars.size(), result.size());
    }

    //List<Car> findAll();
    //Test find all cars in the database (there is only one car in the database)
    @Test
    void findAllWhenOnlyOneCarInDatabase(){
        Brand brand = new Brand("BrandName");
        CarModel model = new CarModel("ModelName");
        CarImage carPhoto = new CarImage(1L, "fileContent".getBytes());
        FuelType diesel = new FuelType(FuelTypeEnum.FUEL_DIESEL);
        Car car = new Car(brand, model, 1998, 350000, diesel, 110, "1.9 TDI", 200, false, carPhoto);

        List<Car> expectedCar = List.of(car);

        when(carRepository.findAll()).thenReturn(expectedCar);

        List<Car> result = carService.findAll();

        assertEquals(expectedCar.size(), result.size());
        assertTrue(result.contains(car));
    }

    //List<Car> findAll();
    //Test find all cars in the database
    @Test
    void findAllWhenFewCarsInDatabase(){
        Brand brand = new Brand("BrandName");
        Brand brand1 = new Brand("BrandName1");

        CarModel model = new CarModel("ModelName");
        CarModel model1 = new CarModel("ModelName1");

        CarImage carPhoto = new CarImage(1L, "fileContent".getBytes());

        FuelType diesel = new FuelType(FuelTypeEnum.FUEL_DIESEL);
        FuelType gasoline = new FuelType(FuelTypeEnum.FUEL_GASOLINE);

        Car car = new Car(brand, model, 1998, 350000, diesel, 110, "1.9 TDI", 200, false, carPhoto);
        Car car1 = new Car(brand1, model1, 2009, 198000, gasoline, 70, "1.4 TDI", 300, false, carPhoto);

        List<Car> expectedCars = Arrays.asList(car, car1);

        when(carRepository.findAll()).thenReturn(expectedCars);

        List<Car> result = carService.findAll();

        assertEquals(expectedCars.size(), result.size());
        assertTrue(result.contains(car));
        assertTrue(result.contains(car1));
    }

    //List<Car> findAll();
    //Test when no cars in the database
    @Test
    void findAllCarsWhenNoCarsInDatabase(){
        List<Car> expectedCars = Collections.emptyList();

        when(carRepository.findAll()).thenReturn(expectedCars);

        List<Car> result = carService.findAll();

        assertEquals(expectedCars.size(), result.size());
    }

}
