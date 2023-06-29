package com.example.carrentalsystem.repositories;

import com.example.carrentalsystem.models.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long> {

}
