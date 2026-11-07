package com.example.university_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.university_management.model.UniversityUser;

public interface UserRepository
        extends JpaRepository<UniversityUser, Long> {

    Optional<UniversityUser> findByEmail(String email);

    List<UniversityUser> findByRole(String role);

    boolean existsByEmail(String email);

    long countByRole(String role);
}