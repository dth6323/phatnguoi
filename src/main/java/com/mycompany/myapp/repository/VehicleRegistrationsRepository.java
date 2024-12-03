package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.VehicleRegistrations;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VehicleRegistrations entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleRegistrationsRepository extends JpaRepository<VehicleRegistrations, Long> {}
