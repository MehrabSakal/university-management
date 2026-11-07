package com.example.university_management.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class UniversityUserTest {

    // Model Unit Test 1: Verify that model getters and setters store values correctly.
    @Test
    void gettersAndSettersWorkCorrectly() {
        UniversityUser user = new UniversityUser();
        user.setId(10L);
        user.setName("Ayesha");
        user.setEmail("ayesha@example.com");
        user.setPassword("hashed-password");
        user.setDepartment("CSE");
        user.setRole("STUDENT");

        assertEquals(10L, user.getId());
        assertEquals("Ayesha", user.getName());
        assertEquals("ayesha@example.com", user.getEmail());
        assertEquals("hashed-password", user.getPassword());
        assertEquals("CSE", user.getDepartment());
        assertEquals("STUDENT", user.getRole());
    }

    // Model Unit Test 2: Password must not appear in JSON responses.
    @Test
    void passwordIsNotIncludedWhenUserIsSerializedToJson() throws Exception {
        UniversityUser user = new UniversityUser();
        user.setId(10L);
        user.setName("Ayesha");
        user.setEmail("ayesha@example.com");
        user.setPassword("secret123");
        user.setDepartment("CSE");
        user.setRole("STUDENT");

        String json = new ObjectMapper().writeValueAsString(user);

        assertFalse(json.contains("password"));
        assertFalse(json.contains("secret123"));
        assertEquals("Ayesha", new ObjectMapper().readTree(json).get("name").asText());
    }
}
