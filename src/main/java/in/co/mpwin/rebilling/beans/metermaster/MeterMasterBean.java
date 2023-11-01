package in.co.mpwin.rebilling.beans.metermaster;

import in.co.mpwin.rebilling.interfaces.metermaster.MeterMasterInterface;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Setter @ToString
@Entity(name="MeterMasterBean")@Table(name="re_meter_master")
public class MeterMasterBean implements MeterMasterInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    Long id;
    @Column(name="meter_number")
    String meterNumber;
    @Column(name="make")
    String make;
    @Column(name="category")
    String category;
    @Column(name="type")
    String type;
    @Column(name="meter_class")
    String meterClass;
    @Column(name="meter_ctr")
    String meterCtr;
    @Column(name="meter_ptr")
    String meterPtr;
    @Column(name="me_ctr")
    String meCtr;
    @Column(name="me_ptr")
    String mePtr;

    @Column(name="dial_bmf")
    String dialBmf;
    @Column(name="equip_class")
    String equipClass;
    @Column(name="phase")
    String phase;
    @Column(name="metergrp")
    String meterGroup;


    @Column(name="mf")
    @DecimalMin(value = "0.0",inclusive = true)
    BigDecimal mf;

    @Column(name="install_date")
    String installDate;
    @Column(name="created_by")
    String createdBy;
    @Column(name="updated_by")
    String updatedBy;
    @Column(name="created_on")
    Timestamp createdOn;
    @Column(name="updated_on")
    Timestamp updatedOn;
    @Column(name="status")
    String status;
    @Column(name="remark")
    String remark;
    @Column(name = "is_mapped")
    String isMapped="no";

    public Long getId() {
        return id;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public String getMake() {
        return make;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public String getMeterClass() {
        return meterClass;
    }

    public String getMeterCtr() {
        return meterCtr;
    }

    public String getMeterPtr() {
        return meterPtr;
    }

    public String getMeCtr() {
        return meCtr;
    }

    public String getMePtr() {
        return mePtr;
    }

    public String getDialBmf() {
        return dialBmf;
    }

    public String getEquipClass() {
        return equipClass;
    }

    public String getPhase() {
        return phase;
    }

    public String getMeterGroup() {
        return meterGroup;
    }

    public BigDecimal getMf() {
        if (this.mf != null)
            return new BigDecimal(String.valueOf(this.mf));
        return mf;
    }

    public String getInstallDate() {
        return installDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public Timestamp getUpdatedOn() {
        return updatedOn;
    }

    public String getStatus() {
        return status;
    }

    public String getRemark() {
        return remark;
    }

    public String getIsMapped() {
        return isMapped;
    }
}
