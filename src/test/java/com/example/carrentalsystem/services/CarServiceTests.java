package com.example.carrentalsystem.services;

import com.example.carrentalsystem.models.*;
import com.example.carrentalsystem.payload.request.AddCarRequest;
import com.example.carrentalsystem.payload.request.EditCarRequest;
import com.example.carrentalsystem.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    //Test when car ID is null
    @Test
    void existsByIdWhenCarIdIsNull(){
        boolean result = carService.existsById(null);
        assertFalse(result);
    }

    //List<Car> findAvailableCars();
    //Test when cars have different availability status
    @Test
    void findAvailableCarsWithMixedAvailability(){
        Brand brand = new Brand("BrandName");

        CarModel model = new CarModel("ModelName");
        CarModel model2 = new CarModel("ModelName2");

        CarImage carPhoto = new CarImage(1L, "fileContent".getBytes());

        FuelType diesel = new FuelType(FuelTypeEnum.FUEL_DIESEL);

        Car availableCar = new Car(brand, model, 1998, 350000, diesel, 110, "1.9 TDI", 200, true, carPhoto);
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
    //Test when there no cars in the database
    @Test
    void findAllCarsWhenNoCarsInDatabase(){
        List<Car> expectedCars = Collections.emptyList();

        when(carRepository.findAll()).thenReturn(expectedCars);

        List<Car> result = carService.findAll();

        assertEquals(expectedCars.size(), result.size());
    }

    //Car getCarById(Long carID);
    //Test when no car with the given ID is found
    @Test
    void getCarByIdWhenNotFound(){
        Long carID = 1L;

        when(carRepository.getCarById(carID)).thenReturn(null);

        Car result = carService.getCarById(carID);

        assertNull(result);
    }

    //Car getCarById(Long carID);
    //Test when given car ID is null
    @Test
    void getCarByIdWhenIdIsNull(){
        Car result = carService.getCarById(null);
        assertNull(result);
    }

    //Car getCarById(Long carID);
    //Test when car ID is correct
    @Test
    void getCarById(){
        Long carID = 1L;
        Brand brand = new Brand("CarBrand");
        CarModel model = new CarModel("CarModel");
        FuelType fuelType = new FuelType(FuelTypeEnum.FUEL_GASOLINE);
        CarImage carImage = new CarImage(1L, "fileContent".getBytes());
        Car expectedCar = new Car(1L, brand, model, 2022, 50000, fuelType, 200, "2.0L", 30000, true, carImage);

        when(carRepository.getCarById(carID)).thenReturn(expectedCar);

        Car result = carService.getCarById(carID);

        assertNotNull(result);
        assertEquals(expectedCar, result);
    }


    //void changeImage(Long carID, MultipartFile file) throws IOException;
    //Test when method changes an image with deletion of previous image
    @Test
    public void shouldChangeImage() throws IOException {
        Long carId = 1L;
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "content".getBytes());

        Car existingCar = new Car();
        existingCar.setCarImage(new CarImage(2L, "imageContent".getBytes()));

        when(carRepository.getCarById(carId)).thenReturn(existingCar);
        when(carImageRepository.save(any(CarImage.class))).thenAnswer(invocation -> {
            CarImage savedImage = invocation.getArgument(0);
            savedImage.setImageID(3L);
            return savedImage;
        });

        carService.changeImage(carId, file);

        verify(carRepository, times(1)).getCarById(carId);
        verify(carImageRepository, times(1)).save(any(CarImage.class));
        verify(carRepository, times(1)).save(existingCar);
        verify(carImageRepository, times(1)).deleteById(2L);

        assertEquals(3L, existingCar.getCarImage().getImageID());
    }

    //void changeImage(Long carID, MultipartFile file) throws IOException;
    //Test when method changes an image, but doesn't delete previous image (because id = 1)
    @Test
    public void shouldNotDeleteDefaultImage() throws IOException {
        Long carId = 1L;
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "content".getBytes());

        Car existingCar = new Car();
        existingCar.setCarImage(new CarImage(1L, "imageContent".getBytes()));

        when(carRepository.getCarById(carId)).thenReturn(existingCar);

        carService.changeImage(carId, file);

        verify(carImageRepository, never()).deleteById(anyLong());
    }

    //void delete(Car car);
    //Car removal test with removal of car model and brand.
    @Test
    public void shouldDeleteCarWithModelAndBrand() {
        Brand brand = new Brand(1L, "CarBrand");
        CarModel model = new CarModel(1L, "CarModel");
        FuelType fuelType = new FuelType(FuelTypeEnum.FUEL_GASOLINE);
        CarImage carImage = new CarImage(1L, "fileContent".getBytes());
        Car car = new Car(1L, brand, model, 2022, 50000, fuelType, 200, "2.0L", 30000, true, carImage);

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(carRepository.countByModelName(model.getName())).thenReturn(1L);
        when(carRepository.countByBrandName(brand.getName())).thenReturn(1L);

        carService.delete(car);

        verify(carModelRepository, times(1)).deleteById(model.getId());
        verify(brandRepository, times(1)).deleteById(brand.getId());
        verify(carRepository, times(1)).deleteById(car.getId());
    }

    //void delete(Car car);
    //Car removal test without removal of car models and brands (car model and brand is used by another car)
    @Test
    public void shouldDeleteCarWithoutBrandAndModel() {
        Brand brand = new Brand(1L, "CarBrand");
        CarModel model = new CarModel(1L, "CarModel");
        FuelType fuelType = new FuelType(FuelTypeEnum.FUEL_GASOLINE);
        CarImage carImage = new CarImage(1L, "fileContent".getBytes());
        Car car = new Car(1L, brand, model, 2022, 50000, fuelType, 200, "2.0L", 30000, true, carImage);

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(carRepository.countByModelName(model.getName())).thenReturn(2L);
        when(carRepository.countByBrandName(brand.getName())).thenReturn(2L);

        carService.delete(car);

        verify(carModelRepository, times(0)).deleteById(model.getId());
        verify(brandRepository, times(0)).deleteById(brand.getId());
        verify(carRepository, times(1)).deleteById(car.getId());
    }

    //void changeStatus(Car car);
    //Car availability status change test
    @Test
    public void shouldChangeCarStatusToAvailable() {
        Car car = new Car();
        car.setAvailable(false);

        when(carRepository.save(car)).thenReturn(car);

        carService.changeStatus(car);

        verify(carRepository, times(1)).save(car);
        assert(car.isAvailable());
    }

    //void changeStatus(Car car);
    //Car availability status change test
    @Test
    public void shouldChangeCarStatusToNotAvailable() {
        Car car = new Car();
        car.setAvailable(true);

        when(carRepository.save(car)).thenReturn(car);

        carService.changeStatus(car);

        verify(carRepository, times(1)).save(car);
        assert(!car.isAvailable());
    }

    //void add(AddCarRequest carRequest)
    //Add a car when all request data is correct
    @Test
    void addCarWhenAllDataCorrect() {
        AddCarRequest carRequest = new AddCarRequest(150, 200, 2022, 50000, "brand", "model", "1.4", 1L);

        Brand brand = new Brand(1L, carRequest.getBrand());
        when(brandRepository.findByName(carRequest.getBrand())).thenReturn(brand);

        CarModel model = new CarModel(1L, carRequest.getModel());
        when(carModelRepository.findByName(carRequest.getModel())).thenReturn(model);

        FuelType fuelType = new FuelType(FuelTypeEnum.FUEL_DIESEL);
        when(fuelTypeService.findById(1L)).thenReturn(Optional.of(fuelType));

        CarImage carImage = new CarImage(1L, "image.jpg".getBytes());
        when(carImageRepository.findById(1L)).thenReturn(Optional.of(carImage));

        when(carRepository.save(any(Car.class))).thenReturn(new Car());

        carService.add(carRequest);

        verify(carRepository, times(1)).save(any(Car.class));
    }

    //void add(AddCarRequest carRequest)
    //Add a car when the brand doesn't exist
    @Test
    void addCarWhenBrandDoesntExist() {
        AddCarRequest carRequest = new AddCarRequest(150, 200, 2022, 50000, "brand", "model", "1.4", 1L);

        when(brandRepository.findByName(carRequest.getBrand())).thenReturn(null);

        CarModel model = new CarModel(1L, carRequest.getModel());
        when(carModelRepository.findByName(carRequest.getModel())).thenReturn(model);

        FuelType fuelType = new FuelType(FuelTypeEnum.FUEL_DIESEL);
        when(fuelTypeService.findById(1L)).thenReturn(Optional.of(fuelType));

        CarImage carImage = new CarImage(1L, "image.jpg".getBytes());
        when(carImageRepository.findById(1L)).thenReturn(Optional.of(carImage));

        when(carRepository.save(any(Car.class))).thenReturn(new Car());

        carService.add(carRequest);

        verify(carRepository, times(1)).save(any(Car.class));
    }

    //void add(AddCarRequest carRequest)
    //Add a car when the car model doesn't exist
    @Test
    void addCarWhenCarModelDoesntExist() {
        AddCarRequest carRequest = new AddCarRequest(150, 200, 2022, 50000, "brand", "model", "1.4", 1L);

        Brand brand = new Brand(1L, carRequest.getBrand());
        when(brandRepository.findByName(carRequest.getBrand())).thenReturn(brand);

        when(carModelRepository.findByName(carRequest.getModel())).thenReturn(null);

        FuelType fuelType = new FuelType(FuelTypeEnum.FUEL_DIESEL);
        when(fuelTypeService.findById(1L)).thenReturn(Optional.of(fuelType));

        CarImage carImage = new CarImage(1L, "image.jpg".getBytes());
        when(carImageRepository.findById(1L)).thenReturn(Optional.of(carImage));

        when(carRepository.save(any(Car.class))).thenReturn(new Car());

        carService.add(carRequest);

        verify(carRepository, times(1)).save(any(Car.class));
    }

    //void add(AddCarRequest carRequest)
    //Add a car when the default car image doesn't exist (default car image ID = 1)
    @Test
    void addCarWhenCarImageNotFound() {
        AddCarRequest carRequest = new AddCarRequest(150, 200, 2022, 50000, "brand", "model", "1.4", 1L);

        Brand brand = new Brand(1L, carRequest.getBrand());
        when(brandRepository.findByName(carRequest.getBrand())).thenReturn(brand);

        CarModel model = new CarModel(1L, carRequest.getModel());
        when(carModelRepository.findByName(carRequest.getModel())).thenReturn(model);

        FuelType fuelType = new FuelType(FuelTypeEnum.FUEL_DIESEL);
        when(fuelTypeService.findById(1L)).thenReturn(Optional.of(fuelType));

        when(carImageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> carService.add(carRequest));
    }

    //void add(AddCarRequest carRequest)
    //Add car when fuel type doesn't exist
    @Test
    void addCarWhenFuelTypeNotFound() {
        AddCarRequest carRequest = new AddCarRequest(150, 200, 2022, 50000, "brand", "model", "1.4", 1L);

        Brand brand = new Brand(1L, carRequest.getBrand());
        when(brandRepository.findByName(carRequest.getBrand())).thenReturn(brand);

        CarModel model = new CarModel(1L, carRequest.getModel());
        when(carModelRepository.findByName(carRequest.getModel())).thenReturn(model);

        when(carImageRepository.findById(carRequest.getFuelType())).thenReturn(Optional.empty());

        CarImage carImage = new CarImage(1L, "image.jpg".getBytes());
        when(carImageRepository.findById(1L)).thenReturn(Optional.of(carImage));

        assertThrows(RuntimeException.class, () -> carService.add(carRequest));
    }

    //void update(Long carID, EditCarRequest carRequest)
    //Update of car details, without deleting brand and car model
    @Test
    public void testUpdateCarWithoutDeletingBrandAndCarModel() {
        //New car details
        EditCarRequest carRequest = new EditCarRequest(150, 30000, 2022, 5000, "newBrand", "newModel", "2.5L", 1L);
        FuelType fuelTypeRequest = new FuelType(FuelTypeEnum.FUEL_GASOLINE);
        CarModel carModelRequest = new CarModel(2L, carRequest.getModel());
        Brand brandRequest = new Brand(2L, carRequest.getBrand());

        //Existing car details
        Long carID = 1L;
        Brand brand = new Brand(1L, "brand");
        CarModel carModel = new CarModel(1L, "model");
        FuelType fuelType = new FuelType(FuelTypeEnum.FUEL_DIESEL);
        Car car = new Car(brand, carModel, 2020, 20000, fuelType, 120, "2.0L", 25000, true, null);

        when(fuelTypeService.findById(1L)).thenReturn(Optional.of(fuelTypeRequest));
        when(carRepository.getCarById(carID)).thenReturn(car);
        when(carRepository.save(any(Car.class))).thenReturn(car);
        when(carModelRepository.findByName(carRequest.getModel())).thenReturn(carModelRequest);
        when(brandRepository.findByName(carRequest.getBrand())).thenReturn(brandRequest);
        when(carRepository.existsByBrandName(brand.getName())).thenReturn(true);
        when(carRepository.existsByModelName(carModel.getName())).thenReturn(true);

        carService.update(carID, carRequest);

        verify(carRepository, times(1)).getCarById(carID);
        verify(carRepository, times(1)).save(car);
        verify(brandRepository, times(0)).deleteByName(anyString());
        verify(carModelRepository, times(0)).deleteByName(anyString());
        verify(fuelTypeRepository, times(1)).findById(anyLong());

        assertEquals(carRequest.getHorsePower(), car.getHorsePower());
        assertEquals(carRequest.getPrice(), car.getPrice());
        assertEquals(carRequest.getYear(), car.getYear());
        assertEquals(carRequest.getMileage(), car.getMileage());
        assertEquals(carRequest.getBrand(), car.getBrand().getName());
        assertEquals(carRequest.getModel(), car.getModel().getName());
        assertEquals(carRequest.getCapacity(), car.getCapacity());
        assertEquals(fuelTypeRequest.getName(), car.getFuelType().getName());
    }

    //void update(Long carID, EditCarRequest carRequest)
    //Update of car details, with deleting brand and car model
    @Test
    public void testUpdateCarWithDeletingBrandAndCarModel() {
        //New car details
        EditCarRequest carRequest = new EditCarRequest(150, 30000, 2022, 5000, "newBrand", "newModel", "2.5L", 1L);
        FuelType fuelTypeRequest = new FuelType(FuelTypeEnum.FUEL_GASOLINE);
        CarModel carModelRequest = new CarModel(2L, carRequest.getModel());
        Brand brandRequest = new Brand(2L, carRequest.getBrand());

        //Existing car details
        Long carID = 1L;
        Brand brand = new Brand(1L, "brand");
        CarModel carModel = new CarModel(1L, "model");
        FuelType fuelType = new FuelType(FuelTypeEnum.FUEL_DIESEL);
        Car car = new Car(brand, carModel, 2020, 20000, fuelType, 120, "2.0L", 25000, true, null);

        when(fuelTypeService.findById(1L)).thenReturn(Optional.of(fuelTypeRequest));
        when(carRepository.getCarById(carID)).thenReturn(car);
        when(carRepository.save(any(Car.class))).thenReturn(car);
        when(carModelRepository.findByName(carRequest.getModel())).thenReturn(carModelRequest);
        when(brandRepository.findByName(carRequest.getBrand())).thenReturn(brandRequest);
        when(carRepository.existsByBrandName(brand.getName())).thenReturn(false);
        when(carRepository.existsByModelName(carModel.getName())).thenReturn(false);

        carService.update(carID, carRequest);

        verify(carRepository, times(1)).getCarById(carID);
        verify(carRepository, times(1)).save(car);
        verify(brandRepository, times(1)).deleteByName(anyString());
        verify(carModelRepository, times(1)).deleteByName(anyString());
        verify(fuelTypeRepository, times(1)).findById(anyLong());

        assertEquals(carRequest.getHorsePower(), car.getHorsePower());
        assertEquals(carRequest.getPrice(), car.getPrice());
        assertEquals(carRequest.getYear(), car.getYear());
        assertEquals(carRequest.getMileage(), car.getMileage());
        assertEquals(carRequest.getBrand(), car.getBrand().getName());
        assertEquals(carRequest.getModel(), car.getModel().getName());
        assertEquals(carRequest.getCapacity(), car.getCapacity());
        assertEquals(fuelTypeRequest.getName(), car.getFuelType().getName());
    }

}
