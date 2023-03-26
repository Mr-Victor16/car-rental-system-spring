package com.example.carrentalsystem.Repositories;

import com.example.carrentalsystem.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    User getUserByUsername(String username);

    User getUserByToken(String token);

    boolean existsByToken(String token);

    User getByToken(String token);
}
