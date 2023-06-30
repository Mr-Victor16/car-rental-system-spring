package com.example.carrentalsystem.repositories;

import com.example.carrentalsystem.models.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    void deleteByName(String name);

    Brand findByName(String name);

}
