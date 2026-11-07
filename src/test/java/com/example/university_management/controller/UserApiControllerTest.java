package com.example.university_management.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.university_management.model.UniversityUser;
import com.example.university_management.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserApiControllerTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserApiController controller;

    // Controller Unit Test 1: Valid student creation returns HTTP 201.
    @Test
    void createStudentWithValidDataReturnsCreated() {
        UniversityUser request = new UniversityUser();
        request.setName("Rahim");
        request.setEmail("rahim@example.com");
        request.setPassword("secret123");
        request.setDepartment("CSE");

        when(repository.existsByEmail("rahim@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("encoded-password");
        when(repository.save(any(UniversityUser.class))).thenAnswer(invocation -> {
            UniversityUser saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        ResponseEntity<?> response = controller.create("students", request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        UniversityUser saved = (UniversityUser) response.getBody();
        assertNotNull(saved);
        assertEquals("STUDENT", saved.getRole());
        assertEquals("encoded-password", saved.getPassword());
        verify(repository).save(any(UniversityUser.class));
    }

    // Controller Unit Test 2: Duplicate email returns HTTP 409 and is not saved.
    @Test
    void createStudentWithExistingEmailReturnsConflict() {
        UniversityUser request = new UniversityUser();
        request.setName("Rahim");
        request.setEmail("rahim@example.com");
        request.setPassword("secret123");

        when(repository.existsByEmail("rahim@example.com")).thenReturn(true);

        ResponseEntity<?> response = controller.create("students", request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(repository, never()).save(any(UniversityUser.class));
    }
}
