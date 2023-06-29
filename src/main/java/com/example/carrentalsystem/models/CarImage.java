package com.example.carrentalsystem.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "car_images")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CarImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageID;

    @Lob
    @Column(columnDefinition="BLOB", nullable = false)
    private byte[] fileContent;

    public CarImage(byte[] fileContent) {
        this.fileContent = fileContent;
    }

}
