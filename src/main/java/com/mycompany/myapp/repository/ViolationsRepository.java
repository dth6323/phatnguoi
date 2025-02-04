package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Violations;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Violations entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ViolationsRepository extends JpaRepository<Violations, Long> {
    @Query("SELECT v FROM Violations v JOIN v.vehicleRegistrations vr WHERE vr.vehicleNumber = :licensePlate")
    List<Violations> findByVehicleLicensePlate(@Param("licensePlate") String licensePlate);

    @Query(
        """
            SELECT v
            FROM Violations v
            JOIN v.vehicleRegistrations vr
            JOIN vr.cccd cc
            WHERE (:sa IS NULL OR :ea IS NULL OR (EXTRACT(YEAR FROM CURRENT_DATE) - EXTRACT(YEAR FROM cc.dateBirth)) BETWEEN :sa AND :ea)
              AND (:d IS NULL OR v.violationTime between :a and :d )
        """
    )
    List<Violations> statisticViolations(
        @Param("a") LocalDate test,
        @Param("d") LocalDate date,
        @Param("sa") Integer startAge,
        @Param("ea") Integer endAge
    );

    @Query(
        value = """
                    Select vr.id from VehicleRegistrations vr
                    where  vr.vehicleNumber = :a
        """
    )
    Integer checkVr(@Param("a") String plate);
}
