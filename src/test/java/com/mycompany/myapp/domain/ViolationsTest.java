package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.TypeViolationTestSamples.*;
import static com.mycompany.myapp.domain.VehicleRegistrationsTestSamples.*;
import static com.mycompany.myapp.domain.ViolationsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ViolationsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Violations.class);
        Violations violations1 = getViolationsSample1();
        Violations violations2 = new Violations();
        assertThat(violations1).isNotEqualTo(violations2);

        violations2.setId(violations1.getId());
        assertThat(violations1).isEqualTo(violations2);

        violations2 = getViolationsSample2();
        assertThat(violations1).isNotEqualTo(violations2);
    }

    @Test
    void typeViolationTest() {
        Violations violations = getViolationsRandomSampleGenerator();
        TypeViolation typeViolationBack = getTypeViolationRandomSampleGenerator();

        violations.setTypeViolation(typeViolationBack);
        assertThat(violations.getTypeViolation()).isEqualTo(typeViolationBack);

        violations.typeViolation(null);
        assertThat(violations.getTypeViolation()).isNull();
    }

    @Test
    void vehicleRegistrationsTest() {
        Violations violations = getViolationsRandomSampleGenerator();
        VehicleRegistrations vehicleRegistrationsBack = getVehicleRegistrationsRandomSampleGenerator();

        violations.setVehicleRegistrations(vehicleRegistrationsBack);
        assertThat(violations.getVehicleRegistrations()).isEqualTo(vehicleRegistrationsBack);

        violations.vehicleRegistrations(null);
        assertThat(violations.getVehicleRegistrations()).isNull();
    }
}
