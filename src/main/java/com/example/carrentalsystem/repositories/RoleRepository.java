package com.example.carrentalsystem.repositories;

import com.example.carrentalsystem.models.RoleEnum;
import com.example.carrentalsystem.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleEnum name);

}
