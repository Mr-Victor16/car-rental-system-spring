package com.example.carrentalsystem.services;

import com.example.carrentalsystem.models.*;
import com.example.carrentalsystem.payload.request.AddCarRentalRequest;
import com.example.carrentalsystem.payload.request.EditCarRentalRequest;
import com.example.carrentalsystem.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RentalServiceTests {
    private RentalService rentalService;
    private RentalRepository rentalRepository;
    private StatusHistoryRepository statusHistoryRepository;
    private RentalStatusServiceImpl rentalStatusService;
    private UserServiceImpl userService;
    private CarRepository carRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
        rentalRepository = mock(RentalRepository.class);
        statusHistoryRepository = mock(StatusHistoryRepository.class);
        RentalStatusRepository rentalStatusRepository = mock(RentalStatusRepository.class);
        rentalStatusService = new RentalStatusServiceImpl(rentalStatusRepository);
        userRepository = mock(UserRepository.class);
        RoleRepository roleRepository = mock(RoleRepository.class);
        userService = new UserServiceImpl(userRepository, null, null, roleRepository, null);
        carRepository = mock(CarRepository.class);
        rentalService = new RentalServiceImpl(rentalRepository, statusHistoryRepository, userRepository, carRepository, rentalStatusRepository);
    }

    //void changeStatus(Long statusID, Long rentalID);
    //Method test when rental status doesn't exist
    @Test
    public void changeStatusWhenRentalStatusNotExist() {
        Long nonExistingStatusId = 999L;
        Long rentalID = 1L;

        when(rentalRepository.getReferenceById(rentalID)).thenReturn(new Rental());
        when(rentalStatusService.findById(nonExistingStatusId)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> rentalService.changeStatus(nonExistingStatusId, rentalID));
    }

    //void changeStatus(Long statusID, Long rentalID);
    //Method test when all data is correct
    @Test
    public void changeStatusWhenRentalAndStatusExist() {
        Long statusID = 1L;
        Long rentalID = 2L;

        Rental rental = new Rental(new Car(), LocalDate.of(2023, 1, 1), LocalDate.of(2026, 1, 10), LocalDate.now(), 2000L, new RentalStatus(RentalStatusEnum.STATUS_PENDING));
        RentalStatus newRentalStatus = new RentalStatus(RentalStatusEnum.STATUS_ACCEPTED);

        when(rentalRepository.getReferenceById(rentalID)).thenReturn(rental);
        when(rentalStatusService.findById(statusID)).thenReturn(Optional.of(newRentalStatus));

        rentalService.changeStatus(statusID, rentalID);

        verify(rentalRepository, times(1)).getReferenceById(rentalID);
        verify(rentalRepository, times(1)).save(rental);

        assertEquals(newRentalStatus, rental.getRentalStatus());
        assertFalse(rental.getStatusHistory().isEmpty());
    }

    //boolean existsDateAndStatus(LocalDate date, RentalStatusEnum status);
    //Test method when all data is correct
    @Test
    public void existsDateAndStatus() {
        LocalDate testDate = LocalDate.of(2023, 1, 1);
        RentalStatusEnum rentalStatusEnum = RentalStatusEnum.STATUS_PENDING;

        when(rentalRepository.existsByRentalDateAndRentalStatus(testDate, rentalStatusEnum)).thenReturn(true);

        boolean existsDateAndStatus = rentalService.existsDateAndStatus(testDate, rentalStatusEnum);

        verify(rentalRepository, times(1)).existsByRentalDateAndRentalStatus(testDate, rentalStatusEnum);
        assertTrue(existsDateAndStatus);
    }

    //boolean existsDateAndStatus(LocalDate date, RentalStatusEnum status);
    //Test method when Date and Rental Status not exist
    @Test
    public void notExistsDateAndStatus() {
        LocalDate nonExistingDate = LocalDate.of(2023, 1, 1);
        RentalStatusEnum nonExistingStatus = RentalStatusEnum.STATUS_PENDING;

        when(rentalRepository.existsByRentalDateAndRentalStatus(nonExistingDate, nonExistingStatus)).thenReturn(false);

        boolean existsDateAndStatus = rentalService.existsDateAndStatus(nonExistingDate, nonExistingStatus);

        verify(rentalRepository, times(1)).existsByRentalDateAndRentalStatus(nonExistingDate, nonExistingStatus);
        assertFalse(existsDateAndStatus);
    }

    //void add(AddCarRentalRequest request)
    //Test method when all data is correct
    @Test
    public void testAddRental() {
        AddCarRentalRequest request = new AddCarRentalRequest(1L, 2L, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 5), LocalDate.now());
        Car car = new Car(1L, new Brand(1L, "CarBrand"), new CarModel(1L, "CarModel"), 2022, 50000, null, 200, "2.0L", 300, true, null);

        when(carRepository.getReferenceById(1L)).thenReturn(car);
        when(userService.getUserById(2L)).thenReturn(new User());
        when(rentalStatusService.findByName(RentalStatusEnum.STATUS_PENDING)).thenReturn(new RentalStatus());
        StatusHistory statusHistory = new StatusHistory(null, LocalDate.now());
        when(statusHistoryRepository.save(statusHistory)).thenReturn(statusHistory);

        rentalService.add(request);

        verify(userRepository, times(1)).save(any(User.class));
        verify(statusHistoryRepository, times(1)).save(any(StatusHistory.class));
    }

    //void delete(Long rentalID);
    //Test method when rental has status history
    @Test
    public void testDeleteRentalWithStatusHistory() {
        Long rentalId = 1L;

        Rental rental = new Rental(new Car(), LocalDate.of(2023, 1, 1), LocalDate.of(2026, 1, 10), LocalDate.now(), 2000L, new RentalStatus(RentalStatusEnum.STATUS_PENDING));
        List<StatusHistory> historyList = List.of(new StatusHistory(1L, new RentalStatus(RentalStatusEnum.STATUS_PENDING), LocalDate.now()));
        rental.setStatusHistory(historyList);
        
        when(rentalService.findById(rentalId)).thenReturn(rental);
        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));

        rentalService.delete(rentalId);

        verify(rentalRepository, times(1)).deleteById(rentalId);
    }

    //void delete(Long rentalID);
    //Test method when rental hasn't status history
    @Test
    public void testDeleteRentalWithoutStatusHistory() {
        Long rentalId = 1L;

        Rental rental = new Rental(new Car(), LocalDate.of(2023, 1, 1), LocalDate.of(2026, 1, 10), LocalDate.now(), 2000L, new RentalStatus(RentalStatusEnum.STATUS_PENDING));

        when(rentalService.findById(rentalId)).thenReturn(rental);
        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));

        rentalService.delete(rentalId);

        verify(rentalRepository, times(1)).deleteById(rentalId);
    }

    //void update(Long rentalID, EditCarRentalRequest request);
    //Test method when all specified data are OK
    @Test
    public void testUpdateWhenAllDataIsOK(){
        Brand brand = new Brand(1L, "CarBrand");
        CarModel model = new CarModel(1L, "CarModel");
        FuelType fuelType = new FuelType(FuelTypeEnum.FUEL_GASOLINE);
        Car car = new Car(1L, brand, model, 2022, 50000, fuelType, 200, "2.0L", 30000, true, null);
        Rental rental = new Rental(car, LocalDate.of(2023, 1, 1), LocalDate.of(2026, 1, 10), LocalDate.now(), 2000L, new RentalStatus(RentalStatusEnum.STATUS_PENDING));

        EditCarRentalRequest request = new EditCarRentalRequest(LocalDate.of(2023,10,12), LocalDate.of(2023,10,15));

        when(rentalRepository.getReferenceById(1L)).thenReturn(rental);

        rentalService.update(1L, request);

        assertEquals(request.getStartDate(), rental.getStartDate());
        assertEquals(request.getEndDate(), rental.getEndDate());
        assertEquals((ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1) * car.getPrice(), rental.getPrice());
    }

    //void update(Long rentalID, EditCarRentalRequest request);
    //Test method when start and end date are the same
    @Test
    public void testUpdateWhenStartAndEndDatesAreTheSame(){
        Brand brand = new Brand(1L, "CarBrand");
        CarModel model = new CarModel(1L, "CarModel");
        FuelType fuelType = new FuelType(FuelTypeEnum.FUEL_GASOLINE);
        Car car = new Car(1L, brand, model, 2022, 50000, fuelType, 200, "2.0L", 30000, true, null);
        Rental rental = new Rental(car, LocalDate.of(2023, 1, 1), LocalDate.of(2026, 1, 10), LocalDate.now(), 2000L, new RentalStatus(RentalStatusEnum.STATUS_PENDING));

        EditCarRentalRequest request = new EditCarRentalRequest(LocalDate.of(2023,10,12), LocalDate.of(2023,10,12));

        when(rentalRepository.getReferenceById(1L)).thenReturn(rental);

        rentalService.update(1L, request);

        assertEquals(request.getStartDate(), rental.getStartDate());
        assertEquals(request.getEndDate(), rental.getEndDate());
        assertEquals(car.getPrice().longValue(), rental.getPrice());
    }

}
