package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Violations;
import com.mycompany.myapp.repository.ViolationsRepository;
import com.mycompany.myapp.service.ViolationsClientService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Violations}.
 */
@RestController
@RequestMapping("/api/violations")
@Transactional
public class ViolationsResource {

    private static final Logger LOG = LoggerFactory.getLogger(ViolationsResource.class);

    private static final String ENTITY_NAME = "violations";
    private final ViolationsClientService violationsClientService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ViolationsRepository violationsRepository;

    public ViolationsResource(ViolationsRepository violationsRepository, ViolationsClientService violationsClientService) {
        this.violationsRepository = violationsRepository;
        this.violationsClientService = violationsClientService;
    }

    @GetMapping("/checkvr")
    public Integer check(@RequestParam String licensePlate) {
        return violationsRepository.checkVr(licensePlate);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/search-by-plate")
    public List<Violations> searchByLicensePlate(@RequestParam String licensePlate) {
        return violationsClientService.searchByLicensePlate(licensePlate);
    }

    @GetMapping("/statical")
    public List<Violations> statical(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate a,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate d,
        @RequestParam(required = false) String sa,
        @RequestParam(required = false) String ea
    ) {
        Integer startAge = (sa != null) ? Integer.parseInt(sa) : null;
        Integer endAge = (ea != null) ? Integer.parseInt(ea) : null;
        return violationsRepository.statisticViolations(a, d, startAge, endAge);
    }

    @PostMapping("")
    public ResponseEntity<Violations> createViolations(@RequestBody Violations violations) throws URISyntaxException {
        LOG.debug("REST request to save Violations : {}", violations);
        if (violations.getId() != null) {
            throw new BadRequestAlertException("A new violations cannot already have an ID", ENTITY_NAME, "idexists");
        }
        violations = violationsRepository.save(violations);
        return ResponseEntity.created(new URI("/api/violations/" + violations.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, violations.getId().toString()))
            .body(violations);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Violations> updateViolations(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Violations violations
    ) throws URISyntaxException {
        LOG.debug("REST request to update Violations : {}, {}", id, violations);
        if (violations.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, violations.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!violationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        violations = violationsRepository.save(violations);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, violations.getId().toString()))
            .body(violations);
    }

    /**
     * {@code PATCH  /violations/:id} : Partial updates given fields of an existing violations, field will ignore if it is null
     *
     * @param id the id of the violations to save.
     * @param violations the violations to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated violations,
     * or with status {@code 400 (Bad Request)} if the violations is not valid,
     * or with status {@code 404 (Not Found)} if the violations is not found,
     * or with status {@code 500 (Internal Server Error)} if the violations couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Violations> partialUpdateViolations(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Violations violations
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Violations partially : {}, {}", id, violations);
        if (violations.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, violations.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!violationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Violations> result = violationsRepository
            .findById(violations.getId())
            .map(existingViolations -> {
                if (violations.getViolationTime() != null) {
                    existingViolations.setViolationTime(violations.getViolationTime());
                }
                if (violations.getLocation() != null) {
                    existingViolations.setLocation(violations.getLocation());
                }
                if (violations.getStatus() != null) {
                    existingViolations.setStatus(violations.getStatus());
                }
                if (violations.getEvidenceImage() != null) {
                    existingViolations.setEvidenceImage(violations.getEvidenceImage());
                }
                if (violations.getCreatedAt() != null) {
                    existingViolations.setCreatedAt(violations.getCreatedAt());
                }

                return existingViolations;
            })
            .map(violationsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, violations.getId().toString())
        );
    }

    /**
     * {@code GET  /violations} : get all the violations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of violations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Violations>> getAllViolations(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Violations");
        Page<Violations> page = violationsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /violations/:id} : get the "id" violations.
     *
     * @param id the id of the violations to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the violations, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Violations> getViolations(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Violations : {}", id);
        Optional<Violations> violations = violationsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(violations);
    }

    /**
     * {@code DELETE  /violations/:id} : delete the "id" violations.
     *
     * @param id the id of the violations to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteViolations(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Violations : {}", id);
        violationsRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
