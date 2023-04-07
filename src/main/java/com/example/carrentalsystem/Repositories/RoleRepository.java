package com.example.carrentalsystem.Repositories;

import com.example.carrentalsystem.Models.ERole;
import com.example.carrentalsystem.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);

    Role getByName(ERole name);

}
