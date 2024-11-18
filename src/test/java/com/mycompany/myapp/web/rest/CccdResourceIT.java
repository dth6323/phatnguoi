package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.CCCDAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Cccd;
import com.mycompany.myapp.repository.CccdRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CCCDResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CccdResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_SEX = "AAAAAAAAAA";
    private static final String UPDATED_SEX = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONALITY = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITY = "BBBBBBBBBB";

    private static final String DEFAULT_PLACE_ORIGIN = "AAAAAAAAAA";
    private static final String UPDATED_PLACE_ORIGIN = "BBBBBBBBBB";

    private static final String DEFAULT_PLACE_RESIDENCE = "AAAAAAAAAA";
    private static final String UPDATED_PLACE_RESIDENCE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_ISSUE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ISSUE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_EXPIRY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_EXPIRY = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PERSONAL_IDENTIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_PERSONAL_IDENTIFICATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cccds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CccdRepository cCCDRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCCCDMockMvc;

    private Cccd cCCD;

    private Cccd insertedCccd;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cccd createEntity() {
        return new Cccd()
            .fullName(DEFAULT_FULL_NAME)
            .dateBirth(DEFAULT_DATE_BIRTH)
            .sex(DEFAULT_SEX)
            .nationality(DEFAULT_NATIONALITY)
            .placeOrigin(DEFAULT_PLACE_ORIGIN)
            .placeResidence(DEFAULT_PLACE_RESIDENCE)
            .dateIssue(DEFAULT_DATE_ISSUE)
            .dateExpiry(DEFAULT_DATE_EXPIRY)
            .personalIdentification(DEFAULT_PERSONAL_IDENTIFICATION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cccd createUpdatedEntity() {
        return new Cccd()
            .fullName(UPDATED_FULL_NAME)
            .dateBirth(UPDATED_DATE_BIRTH)
            .sex(UPDATED_SEX)
            .nationality(UPDATED_NATIONALITY)
            .placeOrigin(UPDATED_PLACE_ORIGIN)
            .placeResidence(UPDATED_PLACE_RESIDENCE)
            .dateIssue(UPDATED_DATE_ISSUE)
            .dateExpiry(UPDATED_DATE_EXPIRY)
            .personalIdentification(UPDATED_PERSONAL_IDENTIFICATION);
    }

    @BeforeEach
    public void initTest() {
        cCCD = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCccd != null) {
            cCCDRepository.delete(insertedCccd);
            insertedCccd = null;
        }
    }

    @Test
    @Transactional
    void createCCCD() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CCCD
        var returnedCCCD = om.readValue(
            restCCCDMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cCCD)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Cccd.class
        );

        // Validate the CCCD in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCCCDUpdatableFieldsEquals(returnedCCCD, getPersistedCCCD(returnedCCCD));

        insertedCccd = returnedCCCD;
    }

    @Test
    @Transactional
    void createCCCDWithExistingId() throws Exception {
        // Create the CCCD with an existing ID
        cCCD.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCCCDMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cCCD)))
            .andExpect(status().isBadRequest());

        // Validate the CCCD in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCCCDS() throws Exception {
        // Initialize the database
        insertedCccd = cCCDRepository.saveAndFlush(cCCD);

        // Get all the cCCDList
        restCCCDMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cCCD.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].dateBirth").value(hasItem(DEFAULT_DATE_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX)))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY)))
            .andExpect(jsonPath("$.[*].placeOrigin").value(hasItem(DEFAULT_PLACE_ORIGIN)))
            .andExpect(jsonPath("$.[*].placeResidence").value(hasItem(DEFAULT_PLACE_RESIDENCE)))
            .andExpect(jsonPath("$.[*].dateIssue").value(hasItem(DEFAULT_DATE_ISSUE.toString())))
            .andExpect(jsonPath("$.[*].dateExpiry").value(hasItem(DEFAULT_DATE_EXPIRY.toString())))
            .andExpect(jsonPath("$.[*].personalIdentification").value(hasItem(DEFAULT_PERSONAL_IDENTIFICATION)));
    }

    @Test
    @Transactional
    void getCCCD() throws Exception {
        // Initialize the database
        insertedCccd = cCCDRepository.saveAndFlush(cCCD);

        // Get the cCCD
        restCCCDMockMvc
            .perform(get(ENTITY_API_URL_ID, cCCD.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cCCD.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.dateBirth").value(DEFAULT_DATE_BIRTH.toString()))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX))
            .andExpect(jsonPath("$.nationality").value(DEFAULT_NATIONALITY))
            .andExpect(jsonPath("$.placeOrigin").value(DEFAULT_PLACE_ORIGIN))
            .andExpect(jsonPath("$.placeResidence").value(DEFAULT_PLACE_RESIDENCE))
            .andExpect(jsonPath("$.dateIssue").value(DEFAULT_DATE_ISSUE.toString()))
            .andExpect(jsonPath("$.dateExpiry").value(DEFAULT_DATE_EXPIRY.toString()))
            .andExpect(jsonPath("$.personalIdentification").value(DEFAULT_PERSONAL_IDENTIFICATION));
    }

    @Test
    @Transactional
    void getNonExistingCCCD() throws Exception {
        // Get the cCCD
        restCCCDMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCCCD() throws Exception {
        // Initialize the database
        insertedCccd = cCCDRepository.saveAndFlush(cCCD);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cCCD
        Cccd updatedCccd = cCCDRepository.findById(cCCD.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCCCD are not directly saved in db
        em.detach(updatedCccd);
        updatedCccd
            .fullName(UPDATED_FULL_NAME)
            .dateBirth(UPDATED_DATE_BIRTH)
            .sex(UPDATED_SEX)
            .nationality(UPDATED_NATIONALITY)
            .placeOrigin(UPDATED_PLACE_ORIGIN)
            .placeResidence(UPDATED_PLACE_RESIDENCE)
            .dateIssue(UPDATED_DATE_ISSUE)
            .dateExpiry(UPDATED_DATE_EXPIRY)
            .personalIdentification(UPDATED_PERSONAL_IDENTIFICATION);

        restCCCDMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCccd.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCccd))
            )
            .andExpect(status().isOk());

        // Validate the CCCD in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCCCDToMatchAllProperties(updatedCccd);
    }

    @Test
    @Transactional
    void putNonExistingCCCD() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cCCD.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCCCDMockMvc
            .perform(put(ENTITY_API_URL_ID, cCCD.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cCCD)))
            .andExpect(status().isBadRequest());

        // Validate the CCCD in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCCCD() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cCCD.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCCCDMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cCCD))
            )
            .andExpect(status().isBadRequest());

        // Validate the CCCD in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCCCD() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cCCD.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCCCDMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cCCD)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CCCD in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCCCDWithPatch() throws Exception {
        // Initialize the database
        insertedCccd = cCCDRepository.saveAndFlush(cCCD);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cCCD using partial update
        Cccd partialUpdatedCccd = new Cccd();
        partialUpdatedCccd.setId(cCCD.getId());

        partialUpdatedCccd.sex(UPDATED_SEX).placeOrigin(UPDATED_PLACE_ORIGIN).dateIssue(UPDATED_DATE_ISSUE).dateExpiry(UPDATED_DATE_EXPIRY);

        restCCCDMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCccd.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCccd))
            )
            .andExpect(status().isOk());

        // Validate the CCCD in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCCCDUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCccd, cCCD), getPersistedCCCD(cCCD));
    }

    @Test
    @Transactional
    void fullUpdateCCCDWithPatch() throws Exception {
        // Initialize the database
        insertedCccd = cCCDRepository.saveAndFlush(cCCD);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cCCD using partial update
        Cccd partialUpdatedCccd = new Cccd();
        partialUpdatedCccd.setId(cCCD.getId());

        partialUpdatedCccd
            .fullName(UPDATED_FULL_NAME)
            .dateBirth(UPDATED_DATE_BIRTH)
            .sex(UPDATED_SEX)
            .nationality(UPDATED_NATIONALITY)
            .placeOrigin(UPDATED_PLACE_ORIGIN)
            .placeResidence(UPDATED_PLACE_RESIDENCE)
            .dateIssue(UPDATED_DATE_ISSUE)
            .dateExpiry(UPDATED_DATE_EXPIRY)
            .personalIdentification(UPDATED_PERSONAL_IDENTIFICATION);

        restCCCDMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCccd.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCccd))
            )
            .andExpect(status().isOk());

        // Validate the CCCD in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCCCDUpdatableFieldsEquals(partialUpdatedCccd, getPersistedCCCD(partialUpdatedCccd));
    }

    @Test
    @Transactional
    void patchNonExistingCCCD() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cCCD.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCCCDMockMvc
            .perform(patch(ENTITY_API_URL_ID, cCCD.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cCCD)))
            .andExpect(status().isBadRequest());

        // Validate the CCCD in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCCCD() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cCCD.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCCCDMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cCCD))
            )
            .andExpect(status().isBadRequest());

        // Validate the CCCD in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCCCD() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cCCD.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCCCDMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cCCD)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CCCD in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCCCD() throws Exception {
        // Initialize the database
        insertedCccd = cCCDRepository.saveAndFlush(cCCD);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cCCD
        restCCCDMockMvc
            .perform(delete(ENTITY_API_URL_ID, cCCD.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cCCDRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Cccd getPersistedCCCD(Cccd cCCD) {
        return cCCDRepository.findById(cCCD.getId()).orElseThrow();
    }

    protected void assertPersistedCCCDToMatchAllProperties(Cccd expectedCccd) {
        assertCCCDAllPropertiesEquals(expectedCccd, getPersistedCCCD(expectedCccd));
    }

    protected void assertPersistedCCCDToMatchUpdatableProperties(Cccd expectedCccd) {
        assertCCCDAllUpdatablePropertiesEquals(expectedCccd, getPersistedCCCD(expectedCccd));
    }
}
