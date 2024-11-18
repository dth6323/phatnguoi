package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CCCDTestSamples.*;
import static com.mycompany.myapp.domain.VehicleRegistrationsTestSamples.*;
import static com.mycompany.myapp.domain.ViolationsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VehicleRegistrationsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleRegistrations.class);
        VehicleRegistrations vehicleRegistrations1 = getVehicleRegistrationsSample1();
        VehicleRegistrations vehicleRegistrations2 = new VehicleRegistrations();
        assertThat(vehicleRegistrations1).isNotEqualTo(vehicleRegistrations2);

        vehicleRegistrations2.setId(vehicleRegistrations1.getId());
        assertThat(vehicleRegistrations1).isEqualTo(vehicleRegistrations2);

        vehicleRegistrations2 = getVehicleRegistrationsSample2();
        assertThat(vehicleRegistrations1).isNotEqualTo(vehicleRegistrations2);
    }

    @Test
    void cCCDTest() {
        VehicleRegistrations vehicleRegistrations = getVehicleRegistrationsRandomSampleGenerator();
        Cccd cCCDBack = getCCCDRandomSampleGenerator();

        vehicleRegistrations.setCCCD(cCCDBack);
        assertThat(vehicleRegistrations.getCCCD()).isEqualTo(cCCDBack);

        vehicleRegistrations.cCCD(null);
        assertThat(vehicleRegistrations.getCCCD()).isNull();
    }

    @Test
    void violationsTest() {
        VehicleRegistrations vehicleRegistrations = getVehicleRegistrationsRandomSampleGenerator();
        Violations violationsBack = getViolationsRandomSampleGenerator();

        vehicleRegistrations.addViolations(violationsBack);
        assertThat(vehicleRegistrations.getViolations()).containsOnly(violationsBack);
        assertThat(violationsBack.getVehicleRegistrations()).isEqualTo(vehicleRegistrations);

        vehicleRegistrations.removeViolations(violationsBack);
        assertThat(vehicleRegistrations.getViolations()).doesNotContain(violationsBack);
        assertThat(violationsBack.getVehicleRegistrations()).isNull();

        vehicleRegistrations.violations(new HashSet<>(Set.of(violationsBack)));
        assertThat(vehicleRegistrations.getViolations()).containsOnly(violationsBack);
        assertThat(violationsBack.getVehicleRegistrations()).isEqualTo(vehicleRegistrations);

        vehicleRegistrations.setViolations(new HashSet<>());
        assertThat(vehicleRegistrations.getViolations()).doesNotContain(violationsBack);
        assertThat(violationsBack.getVehicleRegistrations()).isNull();
    }
}
