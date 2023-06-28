package com.example.carrentalsystem.Repositories;

import com.example.carrentalsystem.Models.ERentalStatus;
import com.example.carrentalsystem.Models.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByUser_Id(Long id);

    List<Rental> findByCar_Id(Long id);

    @Query("select r from Rental r where (r.startDate >= ?1 and r.endDate <= ?1) and r.rentalStatus.name = ?2")
    List<Rental> findByRentalDateAndRentalStatus_Name(LocalDate startDate, ERentalStatus name);

    @Query("select r from Rental r where r.car.id = ?1 and ((r.startDate >= ?2 and r.startDate <= ?3) or (r.endDate >= ?2 and r.endDate <= ?3)) and r.rentalStatus.name = ?4")
    List<Rental> findByCarAndDateAndRentalStatus_Name(Long carId, LocalDate startDate, LocalDate endDate, ERentalStatus name1);

}
