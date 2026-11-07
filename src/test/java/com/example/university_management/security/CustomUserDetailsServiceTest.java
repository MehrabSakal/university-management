package com.example.university_management.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.example.university_management.model.UniversityUser;
import com.example.university_management.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    // Security Unit Test 1: Existing user is converted to Spring Security UserDetails.
    @Test
    void loadUserByUsernameReturnsUserDetailsForExistingUser() {
        UniversityUser user = new UniversityUser();
        user.setEmail("teacher@example.com");
        user.setPassword("encoded-password");
        user.setRole("TEACHER");

        when(userRepository.findByEmail("teacher@example.com"))
                .thenReturn(Optional.of(user));

        UserDetails details = userDetailsService.loadUserByUsername("teacher@example.com");

        assertEquals("teacher@example.com", details.getUsername());
        assertEquals("encoded-password", details.getPassword());
        assertEquals("ROLE_TEACHER", details.getAuthorities().iterator().next().getAuthority());
    }

    // Security Unit Test 2: Unknown email is rejected during authentication lookup.
    @Test
    void loadUserByUsernameThrowsExceptionForUnknownUser() {
        when(userRepository.findByEmail("missing@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("missing@example.com"));
    }
}
