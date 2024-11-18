package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Cccd;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CCCD entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CccdRepository extends JpaRepository<Cccd, Long> {}
