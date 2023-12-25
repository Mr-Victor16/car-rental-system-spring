package com.example.carrentalsystem.services;

import com.example.carrentalsystem.models.*;
import com.example.carrentalsystem.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RentalServiceTests {
    private RentalService rentalService;
    private RentalRepository rentalRepository;
    private StatusHistoryRepository statusHistoryRepository;
    private RentalStatusServiceImpl rentalStatusService;
    private RentalStatusRepository rentalStatusRepository;
    private UserServiceImpl userService;
    private UserRepository userRepository;
    private CarServiceImpl carService;
    private CarRepository carRepository;
    private BrandRepository brandRepository;
    private CarModelRepository carModelRepository;
    private CarImageRepository carImageRepository;
    private FuelServiceImpl fuelTypeService;
    private FuelTypeRepository fuelTypeRepository;
    private RoleRepository roleRepository;
    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp(){
        rentalRepository = mock(RentalRepository.class);
        statusHistoryRepository = mock(StatusHistoryRepository.class);
        rentalStatusRepository = mock(RentalStatusRepository.class);
        rentalStatusService = new RentalStatusServiceImpl(rentalStatusRepository);
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository, null, null, roleService, null);
        carRepository = mock(CarRepository.class);
        brandRepository = mock(BrandRepository.class);
        carModelRepository = mock(CarModelRepository.class);
        carImageRepository = mock(CarImageRepository.class);
        fuelTypeRepository = mock(FuelTypeRepository.class);
        fuelTypeService = new FuelServiceImpl(fuelTypeRepository);
        roleRepository = mock(RoleRepository.class);
        roleService = new RoleServiceImpl(roleRepository);
        carService = new CarServiceImpl(carRepository, brandRepository, carModelRepository, carImageRepository, fuelTypeService);
        rentalService = new RentalServiceImpl(rentalRepository, statusHistoryRepository, rentalStatusService, userService, carService);
    }

    //List<Rental> findAll();
    //Test method when rental list is not empty
    @Test
    public void findAllRentalsWhenListIsNotEmpty() {
        List<Rental> rentalList = Arrays.asList(
                new Rental(new Car(), new User(), LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 10), LocalDate.now(), 2000L, new RentalStatus()),
                new Rental(new Car(), new User(), LocalDate.of(2023, 1, 2), LocalDate.of(2023, 1, 11), LocalDate.now(), 2000L, new RentalStatus()),
                new Rental(new Car(), new User(), LocalDate.of(2023, 1, 3), LocalDate.of(2023, 1, 12), LocalDate.now(), 2000L, new RentalStatus())
        );

        when(rentalRepository.findAll()).thenReturn(rentalList);

        List<Rental> resultRentals = rentalService.findAll();

        verify(rentalRepository, times(1)).findAll();

        assertNotNull(resultRentals);
        assertEquals(rentalList.size(), resultRentals.size());
        assertEquals(rentalList, resultRentals);
    }

    //List<Rental> findAll();
    //Test method when rental list is empty
    @Test
    public void findAllRentalsWhenListIsEmpty() {
        List<Rental> rentalList = Collections.emptyList();

        when(rentalRepository.findAll()).thenReturn(rentalList);

        List<Rental> resultRentals = rentalService.findAll();

        verify(rentalRepository, times(1)).findAll();

        assertNotNull(resultRentals);
        assertEquals(rentalList.size(), resultRentals.size());
        assertEquals(rentalList, resultRentals);
    }

    //List<Rental> findByUserId(Long userID);
    //Test method when user has rentals
    @Test
    public void findByUserIdWhenUserHasRentals() {
        Long userID = 1L;
        List<Rental> listRentalsUser = Arrays.asList(
                new Rental(new Car(), new User(), LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 10), LocalDate.now(), 2000L, new RentalStatus()),
                new Rental(new Car(), new User(), LocalDate.of(2023, 1, 2), LocalDate.of(2023, 1, 11), LocalDate.now(), 2000L, new RentalStatus())
        );

        when(rentalRepository.findByUserId(userID)).thenReturn(listRentalsUser);

        List<Rental> resultRentals = rentalService.findByUserId(userID);

        verify(rentalRepository, times(1)).findByUserId(userID);

        assertNotNull(resultRentals);
        assertEquals(listRentalsUser.size(), resultRentals.size());
        assertEquals(listRentalsUser, resultRentals);
    }

    //List<Rental> findByUserId(Long userID);
    //Test method when user hasn't rentals
    @Test
    public void findByUserIdWhenUserHasNotRentals() {
        Long userID = 1L;
        List<Rental> listRentalsUser = Collections.emptyList();

        when(rentalRepository.findByUserId(userID)).thenReturn(listRentalsUser);

        List<Rental> resultRentals = rentalService.findByUserId(userID);

        verify(rentalRepository, times(1)).findByUserId(userID);

        assertNotNull(resultRentals);
        assertEquals(listRentalsUser.size(), resultRentals.size());
        assertEquals(listRentalsUser, resultRentals);
    }

    //List<Rental> findByUserId(Long userID);
    //Method test when there is more than one user
    @Test
    public void findByUserIdForDifferentUsers() {
        Long userID1 = 1L;
        List<Rental> listRentalsUser1 = Arrays.asList(
                new Rental(new Car(), new User(), LocalDate.of(2023, 1, 1), LocalDate.of(2024, 1, 10), LocalDate.now(), 2000L, new RentalStatus()),
                new Rental(new Car(), new User(), LocalDate.of(2023, 1, 2), LocalDate.of(2025, 1, 11), LocalDate.now(), 2000L, new RentalStatus())
        );

        Long userID2 = 2L;
        List<Rental> listRentalsUser2 = List.of(
                new Rental(new Car(), new User(), LocalDate.of(2023, 1, 1), LocalDate.of(2026, 1, 10), LocalDate.now(), 2000L, new RentalStatus())
        );

        when(rentalRepository.findByUserId(userID1)).thenReturn(listRentalsUser1);
        when(rentalRepository.findByUserId(userID2)).thenReturn(listRentalsUser2);

        List<Rental> resultRentalsForUser1 = rentalService.findByUserId(userID1);
        List<Rental> resultRentalsForUser2 = rentalService.findByUserId(userID2);

        verify(rentalRepository, times(1)).findByUserId(userID1);
        verify(rentalRepository, times(1)).findByUserId(userID2);

        assertNotNull(resultRentalsForUser1);
        assertEquals(listRentalsUser1.size(), resultRentalsForUser1.size());
        assertEquals(listRentalsUser1, resultRentalsForUser1);

        assertNotNull(resultRentalsForUser2);
        assertEquals(listRentalsUser2.size(), resultRentalsForUser2.size());
        assertEquals(listRentalsUser2, resultRentalsForUser2);
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

        Rental rental = new Rental(new Car(), new User(), LocalDate.of(2023, 1, 1), LocalDate.of(2026, 1, 10), LocalDate.now(), 2000L, new RentalStatus(RentalStatusEnum.STATUS_PENDING));
        RentalStatus newRentalStatus = new RentalStatus(RentalStatusEnum.STATUS_ACCEPTED);

        when(rentalRepository.getReferenceById(rentalID)).thenReturn(rental);
        when(rentalStatusService.findById(statusID)).thenReturn(Optional.of(newRentalStatus));

        rentalService.changeStatus(statusID, rentalID);

        verify(rentalRepository, times(1)).getReferenceById(rentalID);
        verify(rentalRepository, times(1)).save(rental);

        assertEquals(newRentalStatus, rental.getRentalStatus());
        assertFalse(rental.getStatusHistory().isEmpty());
    }

    //public boolean existsById(Long rentalID);
    //Test method when rental exists
    @Test
    public void existsByIdWhenRentalExist() {
        Long existingRentalID = 1L;

        when(rentalRepository.existsById(existingRentalID)).thenReturn(true);

        boolean existsForExistingRental = rentalService.existsById(existingRentalID);

        verify(rentalRepository, times(1)).existsById(existingRentalID);
        assertTrue(existsForExistingRental);
    }

    //public boolean existsById(Long rentalID);
    //Test method when rental doesn't exist
    @Test
    public void existsByIdWhenRentalNonExist() {
        Long nonExistingRentalID = 999L;

        when(rentalRepository.existsById(nonExistingRentalID)).thenReturn(false);

        boolean existsForNonExistingRental = rentalService.existsById(nonExistingRentalID);

        verify(rentalRepository, times(1)).existsById(nonExistingRentalID);
        assertFalse(existsForNonExistingRental);
    }

    //public boolean existsById(Long rentalID);
    //Test method when rental ID is null
    @Test
    public void existsByIdWhenRentalIDIsNull() {
        boolean result = rentalService.existsById(null);
        assertFalse(result);
    }

    //public boolean existsById(Long rentalID);
    //Test method when rental ID is negative
    @Test
    public void existsByRentalIdWhenIDIsNegative() {
        Long negativeRentalID = -1L;

        boolean result = rentalService.existsById(negativeRentalID);
        assertFalse(result);
    }

    //boolean existsByCarId(Long id);
    //Test method when all data is correct
    @Test
    public void existsByCarId() {
        Long carId = 1L;

        when(rentalRepository.existsByCarId(carId)).thenReturn(true);

        boolean existsByCarId = rentalService.existsByCarId(carId);

        verify(rentalRepository, times(1)).existsByCarId(carId);
        assertTrue(existsByCarId);
    }

    //boolean existsByCarId(Long id);
    //Test method when car doesn't exist
    @Test
    public void existsByCarIdWhenCarDoesNotExist() {
        Long nonExistingCarId = 999L;

        when(rentalRepository.existsByCarId(nonExistingCarId)).thenReturn(false);

        boolean existsByNonExistingCarId = rentalService.existsByCarId(nonExistingCarId);

        verify(rentalRepository, times(1)).existsByCarId(nonExistingCarId);
        assertFalse(existsByNonExistingCarId);
    }

    //boolean existsByCarId(Long id);
    //Test method when car ID is null
    @Test
    public void existsByCarIsWhenCarIDIsNull() {
        boolean result = rentalService.existsByCarId(null);
        assertFalse(result);
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

    //Rental findById(Long rentalID);
    //Test method when all data is correct
    @Test
    public void findById() {
        Long rentalId = 1L;
        Rental expectedRental = new Rental();

        when(rentalRepository.getReferenceById(rentalId)).thenReturn(expectedRental);

        Rental actualRental = rentalService.findById(rentalId);

        verify(rentalRepository, times(1)).getReferenceById(rentalId);
        assertEquals(expectedRental, actualRental);
    }

    //Rental findById(Long rentalID);
    //Test method when Rental doesn't exist
    @Test
    public void findByIdWhenRentalNotExist() {
        Long nonExistingRentalId = 999L;

        when(rentalRepository.getReferenceById(nonExistingRentalId)).thenReturn(null);

        Rental actualRental = rentalService.findById(nonExistingRentalId);

        verify(rentalRepository, times(1)).getReferenceById(nonExistingRentalId);
        assertNull(actualRental);
    }

    //Rental findById(Long rentalID);
    //Test method when rental ID is null
    @Test
    public void findByIdWhenRentalIDIsNull() {
        Rental result = rentalService.findById(null);
        assertNull(result);
    }

}
