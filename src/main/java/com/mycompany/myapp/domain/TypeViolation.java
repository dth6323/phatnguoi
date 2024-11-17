package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A TypeViolation.
 */
@Entity
@Table(name = "type_violation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeViolation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "violation_name")
    private String violationName;

    @Column(name = "fine_amount", precision = 21, scale = 2)
    private BigDecimal fineAmount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "typeViolation")
    @JsonIgnoreProperties(value = { "typeViolation", "vehicleRegistrations" }, allowSetters = true)
    private Set<Violations> violations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypeViolation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getViolationName() {
        return this.violationName;
    }

    public TypeViolation violationName(String violationName) {
        this.setViolationName(violationName);
        return this;
    }

    public void setViolationName(String violationName) {
        this.violationName = violationName;
    }

    public BigDecimal getFineAmount() {
        return this.fineAmount;
    }

    public TypeViolation fineAmount(BigDecimal fineAmount) {
        this.setFineAmount(fineAmount);
        return this;
    }

    public void setFineAmount(BigDecimal fineAmount) {
        this.fineAmount = fineAmount;
    }

    public Set<Violations> getViolations() {
        return this.violations;
    }

    public void setViolations(Set<Violations> violations) {
        if (this.violations != null) {
            this.violations.forEach(i -> i.setTypeViolation(null));
        }
        if (violations != null) {
            violations.forEach(i -> i.setTypeViolation(this));
        }
        this.violations = violations;
    }

    public TypeViolation violations(Set<Violations> violations) {
        this.setViolations(violations);
        return this;
    }

    public TypeViolation addViolations(Violations violations) {
        this.violations.add(violations);
        violations.setTypeViolation(this);
        return this;
    }

    public TypeViolation removeViolations(Violations violations) {
        this.violations.remove(violations);
        violations.setTypeViolation(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeViolation)) {
            return false;
        }
        return getId() != null && getId().equals(((TypeViolation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeViolation{" +
            "id=" + getId() +
            ", violationName='" + getViolationName() + "'" +
            ", fineAmount=" + getFineAmount() +
            "}";
    }
}
