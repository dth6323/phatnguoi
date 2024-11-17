package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Violations.
 */
@Entity
@Table(name = "violations")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Violations implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "violation_time")
    private LocalDate violationTime;

    @Column(name = "location")
    private String location;

    @Column(name = "status")
    private String status;

    @Column(name = "evidence_image")
    private String evidenceImage;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "violations" }, allowSetters = true)
    private TypeViolation typeViolation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "cCCD", "violations" }, allowSetters = true)
    private VehicleRegistrations vehicleRegistrations;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Violations id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getViolationTime() {
        return this.violationTime;
    }

    public Violations violationTime(LocalDate violationTime) {
        this.setViolationTime(violationTime);
        return this;
    }

    public void setViolationTime(LocalDate violationTime) {
        this.violationTime = violationTime;
    }

    public String getLocation() {
        return this.location;
    }

    public Violations location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return this.status;
    }

    public Violations status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEvidenceImage() {
        return this.evidenceImage;
    }

    public Violations evidenceImage(String evidenceImage) {
        this.setEvidenceImage(evidenceImage);
        return this;
    }

    public void setEvidenceImage(String evidenceImage) {
        this.evidenceImage = evidenceImage;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public Violations createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public TypeViolation getTypeViolation() {
        return this.typeViolation;
    }

    public void setTypeViolation(TypeViolation typeViolation) {
        this.typeViolation = typeViolation;
    }

    public Violations typeViolation(TypeViolation typeViolation) {
        this.setTypeViolation(typeViolation);
        return this;
    }

    public VehicleRegistrations getVehicleRegistrations() {
        return this.vehicleRegistrations;
    }

    public void setVehicleRegistrations(VehicleRegistrations vehicleRegistrations) {
        this.vehicleRegistrations = vehicleRegistrations;
    }

    public Violations vehicleRegistrations(VehicleRegistrations vehicleRegistrations) {
        this.setVehicleRegistrations(vehicleRegistrations);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Violations)) {
            return false;
        }
        return getId() != null && getId().equals(((Violations) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Violations{" +
            "id=" + getId() +
            ", violationTime='" + getViolationTime() + "'" +
            ", location='" + getLocation() + "'" +
            ", status='" + getStatus() + "'" +
            ", evidenceImage='" + getEvidenceImage() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
