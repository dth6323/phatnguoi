package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Cccd;
import com.mycompany.myapp.repository.CccdRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * REST controller for managing {@link Cccd}.
 */
@RestController
@RequestMapping("/api/cccds")
@Transactional
public class CCCDResource {

    private static final Logger LOG = LoggerFactory.getLogger(CCCDResource.class);

    private static final String ENTITY_NAME = "cCCD";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CccdRepository cCCDRepository;

    public CCCDResource(CccdRepository cCCDRepository) {
        this.cCCDRepository = cCCDRepository;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<Cccd> createCCCD(@RequestBody Cccd cCCD) throws URISyntaxException {
        LOG.debug("REST request to save CCCD : {}", cCCD);
        if (cCCD.getId() != null) {
            throw new BadRequestAlertException("A new cCCD cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cCCD = cCCDRepository.save(cCCD);
        return ResponseEntity.created(new URI("/api/cccds/" + cCCD.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cCCD.getId().toString()))
            .body(cCCD);
    }

    /**
     * {@code PUT  /cccds/:id} : Updates an existing cCCD.
     *
     * @param id the id of the cCCD to save.
     * @param cCCD the cCCD to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cCCD,
     * or with status {@code 400 (Bad Request)} if the cCCD is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cCCD couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Cccd> updateCCCD(@PathVariable(value = "id", required = false) final Long id, @RequestBody Cccd cCCD)
        throws URISyntaxException {
        LOG.debug("REST request to update CCCD : {}, {}", id, cCCD);
        if (cCCD.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cCCD.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cCCDRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cCCD = cCCDRepository.save(cCCD);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cCCD.getId().toString()))
            .body(cCCD);
    }

    /**
     * {@code PATCH  /cccds/:id} : Partial updates given fields of an existing cCCD, field will ignore if it is null
     *
     * @param id the id of the cCCD to save.
     * @param cCCD the cCCD to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cCCD,
     * or with status {@code 400 (Bad Request)} if the cCCD is not valid,
     * or with status {@code 404 (Not Found)} if the cCCD is not found,
     * or with status {@code 500 (Internal Server Error)} if the cCCD couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cccd> partialUpdateCCCD(@PathVariable(value = "id", required = false) final Long id, @RequestBody Cccd cCCD)
        throws URISyntaxException {
        LOG.debug("REST request to partial update CCCD partially : {}, {}", id, cCCD);
        if (cCCD.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cCCD.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cCCDRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cccd> result = cCCDRepository
            .findById(cCCD.getId())
            .map(existingCCCD -> {
                if (cCCD.getFullName() != null) {
                    existingCCCD.setFullName(cCCD.getFullName());
                }
                if (cCCD.getDateBirth() != null) {
                    existingCCCD.setDateBirth(cCCD.getDateBirth());
                }
                if (cCCD.getSex() != null) {
                    existingCCCD.setSex(cCCD.getSex());
                }
                if (cCCD.getNationality() != null) {
                    existingCCCD.setNationality(cCCD.getNationality());
                }
                if (cCCD.getPlaceOrigin() != null) {
                    existingCCCD.setPlaceOrigin(cCCD.getPlaceOrigin());
                }
                if (cCCD.getPlaceResidence() != null) {
                    existingCCCD.setPlaceResidence(cCCD.getPlaceResidence());
                }
                if (cCCD.getDateIssue() != null) {
                    existingCCCD.setDateIssue(cCCD.getDateIssue());
                }
                if (cCCD.getDateExpiry() != null) {
                    existingCCCD.setDateExpiry(cCCD.getDateExpiry());
                }
                if (cCCD.getPersonalIdentification() != null) {
                    existingCCCD.setPersonalIdentification(cCCD.getPersonalIdentification());
                }

                return existingCCCD;
            })
            .map(cCCDRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cCCD.getId().toString())
        );
    }

    /**
     * {@code GET  /cccds} : get all the cCCDS.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cCCDS in body.
     */

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("")
    public ResponseEntity<List<Cccd>> getAllCCCDS(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of CCCDS");
        Page<Cccd> page = cCCDRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cccds/:id} : get the "id" cCCD.
     *
     * @param id the id of the cCCD to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cCCD, or with status {@code 404 (Not Found)}.
     */

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Cccd> getCCCD(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CCCD : {}", id);
        Optional<Cccd> cCCD = cCCDRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cCCD);
    }

    /**
     * {@code DELETE  /cccds/:id} : delete the "id" cCCD.
     *
     * @param id the id of the cCCD to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCCCD(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CCCD : {}", id);
        cCCDRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
