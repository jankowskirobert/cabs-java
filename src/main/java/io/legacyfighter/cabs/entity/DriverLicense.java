package io.legacyfighter.cabs.entity;

import javax.persistence.Embeddable;

@Embeddable
public class DriverLicense {
    private String license;
    private static final String DRIVER_LICENSE_REGEX = "^[A-Z9]{5}\\d{6}[A-Z9]{2}\\d[A-Z]{2}$";

    private DriverLicense(String license) {
        this.license = license;
    }

    public DriverLicense() {
    }

    public static DriverLicense withoutValidation(String license) {
        return new DriverLicense(license);
    }

    public static DriverLicense withValidation(String license) {
        if (license == null || license.isEmpty() || !license.matches(DRIVER_LICENSE_REGEX)) {
            throw new IllegalArgumentException("Illegal license no = " + license);
        }
        return new DriverLicense(license);
    }

    public String asString() {
        return license;
    }
}
