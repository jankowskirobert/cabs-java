package io.legacyfighter.cabs.service;

import io.legacyfighter.cabs.entity.Driver;
import io.legacyfighter.cabs.repository.DriverRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ValidateDriverLicenceIntegrationTest {
    @Autowired
    DriverService driverService;

    @Autowired
    DriverRepository driverRepository;

    @BeforeEach
    void clear() {
        driverRepository.deleteAll();
    }

    @Test
    void cannotCreateActiveDriverWithInvalidLicense() {
        Executable test = () -> {
            driverService.createDriver("ABC", "TestLastName", "TestFirstName", Driver.Type.REGULAR, Driver.Status.ACTIVE, "photo?");
        };
        Exception e = assertThrows(IllegalArgumentException.class, test);
        assertEquals("Illegal license no = ABC", e.getMessage());
    }

    @Test
    void cannotCreateActiveDriverWithInvalidPhoto() {
        Executable test = () -> {
            driverService.createDriver("99999740614992TL", "TestLastName", "TestFirstName", Driver.Type.REGULAR, Driver.Status.ACTIVE, "photo?");
        };
        Exception e = assertThrows(IllegalArgumentException.class, test);
        assertEquals("Illegal photo in base64", e.getMessage());
    }

    @Test
    void cannotChangeDriverLicenseNumberDriverNotExists() {
        Executable test = () -> {
            driverService.changeLicenseNumber("99999740614992TW", 1L);
        };
        Exception e = assertThrows(IllegalArgumentException.class, test);
        assertEquals("Driver does not exists, id = 1", e.getMessage());
    }

    @Test
    void cannotChangeDriverLicenseNumber_wrongLicenseNumber() {
        Executable test = () -> {
            driverService.createDriver("99999740614992TL", "TestLastName", "TestFirstName", Driver.Type.REGULAR, Driver.Status.ACTIVE, photo);
            driverService.changeLicenseNumber("ABC", 1L);
        };
        Exception e = assertThrows(IllegalArgumentException.class, test);
        assertEquals("Illegal new license no = ABC", e.getMessage());
    }

    @Test
    void cannotChangeDriverLicenseNumber_driverIsNotInActiveStatus() {
        Executable test = () -> {
            driverService.createDriver("99999740614992TL", "TestLastName", "TestFirstName", Driver.Type.REGULAR, Driver.Status.INACTIVE, photo);
            driverService.changeLicenseNumber("99999740614992TW", 1L);
        };
        Exception e = assertThrows(IllegalStateException.class, test);
        assertEquals("Driver is not active, cannot change license", e.getMessage());
    }

    String photo = "abc===defg\n\r123456\r789\r\rABC\n\nDEF==GHI\r\nJKL==============";
}
