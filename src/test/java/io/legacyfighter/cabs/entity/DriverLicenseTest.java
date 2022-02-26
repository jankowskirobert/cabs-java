package io.legacyfighter.cabs.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.parallel.Execution;

import static org.junit.jupiter.api.Assertions.*;

class DriverLicenseTest {

    @Test
    void withoutValidation() {
        DriverLicense driverLicense = DriverLicense.withoutValidation("ABC");
        assertEquals("ABC", driverLicense.asString());
    }

    @Test
    void withValidation() {
        DriverLicense driverLicense = DriverLicense.withValidation("99999740614992TL");
        assertEquals("99999740614992TL", driverLicense.asString());
    }

    @Test
    void withValidation_incorrectData() {
        Executable executable = () -> {
            DriverLicense driverLicense = DriverLicense.withValidation("ABC");
        };
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("Illegal license no = ABC", exception.getMessage());
    }
}
