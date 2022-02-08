package io.legacyfighter.cabs.service;

import io.legacyfighter.cabs.dto.DriverDTO;
import io.legacyfighter.cabs.entity.Driver;
import io.legacyfighter.cabs.repository.DriverRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
            createDriver("ABC", Driver.Status.ACTIVE, VALID_PHOTO);
        };
        Exception e = assertThrows(IllegalArgumentException.class, test);
        assertEquals("Illegal license no = ABC", e.getMessage());
    }

    @Test
    void cannotCreateActiveDriverWithInvalidPhoto() {
        Executable test = () -> {
            createDriver("99999740614992TL", Driver.Status.ACTIVE, "photo?");
        };
        Exception e = assertThrows(IllegalArgumentException.class, test);
        assertEquals("Illegal photo in base64", e.getMessage());
    }

    @Disabled
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
            Driver driver = createDriver("99999740614992TL", Driver.Status.ACTIVE, VALID_PHOTO);
            driverService.changeLicenseNumber("ABC", driver.getId());
        };
        Exception e = assertThrows(IllegalArgumentException.class, test);
        assertEquals("Illegal new license no = ABC", e.getMessage());
    }

    @Test
    void cannotChangeDriverLicenseNumber_driverIsNotInActiveStatus() {
        Executable test = () -> {
            Driver driver = createDriver("99999740614992TL", Driver.Status.INACTIVE, VALID_PHOTO);
            driverService.changeLicenseNumber("99999740614992TW", driver.getId());
        };
        Exception e = assertThrows(IllegalStateException.class, test);
        assertEquals("Driver is not active, cannot change license", e.getMessage());
    }

    @Test
    void changeDriverStatus() {
        Driver driver = createDriver("99999740614992TL", Driver.Status.INACTIVE, VALID_PHOTO);
        driverService.changeDriverStatus(driver.getId(), Driver.Status.ACTIVE);
        DriverDTO driverDTO = driverService.loadDriver(driver.getId());
        assertEquals(Driver.Status.ACTIVE, driverDTO.getStatus());
    }

    private Driver createDriver(String s, Driver.Status inactive, String photo) {
        return driverService.createDriver(s, "TestLastName", "TestFirstName", Driver.Type.REGULAR, inactive, photo);
    }

    static final String VALID_PHOTO = "abc===defg\n\r123456\r789\r\rABC\n\nDEF==GHI\r\nJKL==============";
}
