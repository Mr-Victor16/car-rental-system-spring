package com.example.carrentalsystem.Repositories;

import com.example.carrentalsystem.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    User getUserByUsername(String username);

    Optional<User> findByUsername(String username);
}
