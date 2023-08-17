package in.co.mpwin.rebilling.beans.metermaster;

import in.co.mpwin.rebilling.interfaces.metermaster.MeterMasterInterface;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.sql.Timestamp;
@Getter
@Setter
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
    @Digits(integer = 38,fraction = 6)
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


}
