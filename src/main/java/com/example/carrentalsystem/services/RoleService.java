package com.example.carrentalsystem.services;

import com.example.carrentalsystem.models.Role;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface RoleService {
    Set<Role> setRole(Set<String> stringRoles);
}
