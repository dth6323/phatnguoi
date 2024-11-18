package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A CCCD.
 */
@Entity
@Table(name = "cccd")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cccd implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "date_birth")
    private LocalDate dateBirth;

    @Column(name = "sex")
    private String sex;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "place_origin")
    private String placeOrigin;

    @Column(name = "place_residence")
    private String placeResidence;

    @Column(name = "date_issue")
    private LocalDate dateIssue;

    @Column(name = "date_expiry")
    private LocalDate dateExpiry;

    @Column(name = "personal_identification")
    private String personalIdentification;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cccd")
    @JsonIgnoreProperties(value = { "cccd", "violations" }, allowSetters = true)
    private Set<VehicleRegistrations> vehicleRegistrations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cccd id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public Cccd fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getDateBirth() {
        return this.dateBirth;
    }

    public Cccd dateBirth(LocalDate dateBirth) {
        this.setDateBirth(dateBirth);
        return this;
    }

    public void setDateBirth(LocalDate dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getSex() {
        return this.sex;
    }

    public Cccd sex(String sex) {
        this.setSex(sex);
        return this;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNationality() {
        return this.nationality;
    }

    public Cccd nationality(String nationality) {
        this.setNationality(nationality);
        return this;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPlaceOrigin() {
        return this.placeOrigin;
    }

    public Cccd placeOrigin(String placeOrigin) {
        this.setPlaceOrigin(placeOrigin);
        return this;
    }

    public void setPlaceOrigin(String placeOrigin) {
        this.placeOrigin = placeOrigin;
    }

    public String getPlaceResidence() {
        return this.placeResidence;
    }

    public Cccd placeResidence(String placeResidence) {
        this.setPlaceResidence(placeResidence);
        return this;
    }

    public void setPlaceResidence(String placeResidence) {
        this.placeResidence = placeResidence;
    }

    public LocalDate getDateIssue() {
        return this.dateIssue;
    }

    public Cccd dateIssue(LocalDate dateIssue) {
        this.setDateIssue(dateIssue);
        return this;
    }

    public void setDateIssue(LocalDate dateIssue) {
        this.dateIssue = dateIssue;
    }

    public LocalDate getDateExpiry() {
        return this.dateExpiry;
    }

    public Cccd dateExpiry(LocalDate dateExpiry) {
        this.setDateExpiry(dateExpiry);
        return this;
    }

    public void setDateExpiry(LocalDate dateExpiry) {
        this.dateExpiry = dateExpiry;
    }

    public String getPersonalIdentification() {
        return this.personalIdentification;
    }

    public Cccd personalIdentification(String personalIdentification) {
        this.setPersonalIdentification(personalIdentification);
        return this;
    }

    public void setPersonalIdentification(String personalIdentification) {
        this.personalIdentification = personalIdentification;
    }

    public Set<VehicleRegistrations> getVehicleRegistrations() {
        return this.vehicleRegistrations;
    }

    public void setVehicleRegistrations(Set<VehicleRegistrations> vehicleRegistrations) {
        if (this.vehicleRegistrations != null) {
            this.vehicleRegistrations.forEach(i -> i.setCCCD(null));
        }
        if (vehicleRegistrations != null) {
            vehicleRegistrations.forEach(i -> i.setCCCD(this));
        }
        this.vehicleRegistrations = vehicleRegistrations;
    }

    public Cccd vehicleRegistrations(Set<VehicleRegistrations> vehicleRegistrations) {
        this.setVehicleRegistrations(vehicleRegistrations);
        return this;
    }

    public Cccd addVehicleRegistrations(VehicleRegistrations vehicleRegistrations) {
        this.vehicleRegistrations.add(vehicleRegistrations);
        vehicleRegistrations.setCCCD(this);
        return this;
    }

    public Cccd removeVehicleRegistrations(VehicleRegistrations vehicleRegistrations) {
        this.vehicleRegistrations.remove(vehicleRegistrations);
        vehicleRegistrations.setCCCD(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cccd)) {
            return false;
        }
        return getId() != null && getId().equals(((Cccd) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CCCD{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", dateBirth='" + getDateBirth() + "'" +
            ", sex='" + getSex() + "'" +
            ", nationality='" + getNationality() + "'" +
            ", placeOrigin='" + getPlaceOrigin() + "'" +
            ", placeResidence='" + getPlaceResidence() + "'" +
            ", dateIssue='" + getDateIssue() + "'" +
            ", dateExpiry='" + getDateExpiry() + "'" +
            ", personalIdentification='" + getPersonalIdentification() + "'" +
            "}";
    }
}
