# Testing Guide

This project contains exactly 6 unit test methods and 2 integration test methods.

## Unit tests (6)

| Category | Test class | Test methods | Count |
|---|---|---|---:|
| Controller | `controller/UserApiControllerTest.java` | Valid student creation; duplicate email rejection | 2 |
| Model | `model/UniversityUserTest.java` | Getters/setters; password excluded from JSON serialization | 2 |
| Security | `security/CustomUserDetailsServiceTest.java` | Existing user loads with role; unknown user throws exception | 2 |

## Integration tests (2)

`integration/UniversityManagementIT.java` contains:

1. A complete student CRUD API workflow using MockMvc, Spring Boot and the H2 test database.
2. A security check that a logged-in Student cannot access the Admin API.

## Run locally on Windows PowerShell

Run all six unit tests:

```powershell
.\mvnw.cmd test
```

Run the two integration tests without repeating unit tests:

```powershell
.\mvnw.cmd -Dskip.unit.tests=true verify
```

Run all eight tests in one Maven build:

```powershell
.\mvnw.cmd clean verify
```

Unit test XML reports are written to `target/surefire-reports/`. Integration test XML reports are written to `target/failsafe-reports/`. GitHub Actions publishes these reports as separate jobs.
