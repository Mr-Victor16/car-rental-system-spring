package com.example.carrentalsystem.Models;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "car_images")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CarImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer imageID;

    @Lob
    @Column(nullable = false)
    private byte[] fileContent;

    public CarImage(byte[] fileContent) {
        this.fileContent = fileContent;
    }

}
