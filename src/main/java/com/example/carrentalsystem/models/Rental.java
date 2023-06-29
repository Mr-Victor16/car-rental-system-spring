package com.example.carrentalsystem.models;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rentals")
@NoArgsConstructor
@Getter
@Setter
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate addDate;

    @NotNull
    private Long price;

    @ManyToOne
    @JoinColumn(name = "rental_status_id")
    private RentalStatus rentalStatus;

    @OneToMany
    @JoinColumn(name = "rental_id")
    private List<StatusHistory> statusHistory = new ArrayList<>();

    public Rental(Car car, User user, LocalDate startDate, LocalDate endDate, LocalDate addDate, Long price, RentalStatus rentalStatus) {
        this.car = car;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.addDate = addDate;
        this.price = price;
        this.rentalStatus = rentalStatus;
    }

    public Rental(Car car, User user, LocalDate startDate, LocalDate endDate, LocalDate addDate, Long price, RentalStatus rentalStatus, List<StatusHistory> statusHistory) {
        this.car = car;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.addDate = addDate;
        this.price = price;
        this.rentalStatus = rentalStatus;
        this.statusHistory = statusHistory;
    }
}
