package com.example.carrentalsystem.Repositories;

import com.example.carrentalsystem.Models.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Brand findByName(String name);
}
