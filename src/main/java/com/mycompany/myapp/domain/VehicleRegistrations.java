package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A VehicleRegistrations.
 */
@Entity
@Table(name = "vehicle_registrations")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleRegistrations implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "vehicle_number")
    private String vehicleNumber;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "engine_num")
    private String engineNum;

    @Column(name = "chassis_num")
    private String chassisNum;

    @Column(name = "vehicle_type")
    private String vehicleType;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model_code")
    private String modelCode;

    @Column(name = "color")
    private String color;

    @Column(name = "capacity")
    private String capacity;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "issued_by")
    private String issuedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "vehicleRegistrations" }, allowSetters = true)
    private CCCD cCCD;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vehicleRegistrations")
    @JsonIgnoreProperties(value = { "typeViolation", "vehicleRegistrations" }, allowSetters = true)
    private Set<Violations> violations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VehicleRegistrations id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVehicleNumber() {
        return this.vehicleNumber;
    }

    public VehicleRegistrations vehicleNumber(String vehicleNumber) {
        this.setVehicleNumber(vehicleNumber);
        return this;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public VehicleRegistrations ownerName(String ownerName) {
        this.setOwnerName(ownerName);
        return this;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getEngineNum() {
        return this.engineNum;
    }

    public VehicleRegistrations engineNum(String engineNum) {
        this.setEngineNum(engineNum);
        return this;
    }

    public void setEngineNum(String engineNum) {
        this.engineNum = engineNum;
    }

    public String getChassisNum() {
        return this.chassisNum;
    }

    public VehicleRegistrations chassisNum(String chassisNum) {
        this.setChassisNum(chassisNum);
        return this;
    }

    public void setChassisNum(String chassisNum) {
        this.chassisNum = chassisNum;
    }

    public String getVehicleType() {
        return this.vehicleType;
    }

    public VehicleRegistrations vehicleType(String vehicleType) {
        this.setVehicleType(vehicleType);
        return this;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getBrand() {
        return this.brand;
    }

    public VehicleRegistrations brand(String brand) {
        this.setBrand(brand);
        return this;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModelCode() {
        return this.modelCode;
    }

    public VehicleRegistrations modelCode(String modelCode) {
        this.setModelCode(modelCode);
        return this;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getColor() {
        return this.color;
    }

    public VehicleRegistrations color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCapacity() {
        return this.capacity;
    }

    public VehicleRegistrations capacity(String capacity) {
        this.setCapacity(capacity);
        return this;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public LocalDate getRegistrationDate() {
        return this.registrationDate;
    }

    public VehicleRegistrations registrationDate(LocalDate registrationDate) {
        this.setRegistrationDate(registrationDate);
        return this;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    public VehicleRegistrations expirationDate(LocalDate expirationDate) {
        this.setExpirationDate(expirationDate);
        return this;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getIssuedBy() {
        return this.issuedBy;
    }

    public VehicleRegistrations issuedBy(String issuedBy) {
        this.setIssuedBy(issuedBy);
        return this;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public CCCD getCCCD() {
        return this.cCCD;
    }

    public void setCCCD(CCCD cCCD) {
        this.cCCD = cCCD;
    }

    public VehicleRegistrations cCCD(CCCD cCCD) {
        this.setCCCD(cCCD);
        return this;
    }

    public Set<Violations> getViolations() {
        return this.violations;
    }

    public void setViolations(Set<Violations> violations) {
        if (this.violations != null) {
            this.violations.forEach(i -> i.setVehicleRegistrations(null));
        }
        if (violations != null) {
            violations.forEach(i -> i.setVehicleRegistrations(this));
        }
        this.violations = violations;
    }

    public VehicleRegistrations violations(Set<Violations> violations) {
        this.setViolations(violations);
        return this;
    }

    public VehicleRegistrations addViolations(Violations violations) {
        this.violations.add(violations);
        violations.setVehicleRegistrations(this);
        return this;
    }

    public VehicleRegistrations removeViolations(Violations violations) {
        this.violations.remove(violations);
        violations.setVehicleRegistrations(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleRegistrations)) {
            return false;
        }
        return getId() != null && getId().equals(((VehicleRegistrations) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleRegistrations{" +
            "id=" + getId() +
            ", vehicleNumber='" + getVehicleNumber() + "'" +
            ", ownerName='" + getOwnerName() + "'" +
            ", engineNum='" + getEngineNum() + "'" +
            ", chassisNum='" + getChassisNum() + "'" +
            ", vehicleType='" + getVehicleType() + "'" +
            ", brand='" + getBrand() + "'" +
            ", modelCode='" + getModelCode() + "'" +
            ", color='" + getColor() + "'" +
            ", capacity='" + getCapacity() + "'" +
            ", registrationDate='" + getRegistrationDate() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", issuedBy='" + getIssuedBy() + "'" +
            "}";
    }
}
