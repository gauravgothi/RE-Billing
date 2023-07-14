package in.co.mpwin.rebilling.beans.metermaster;

import in.co.mpwin.rebilling.interfaces.metermaster.MeterMasterInterface;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
@Getter @Setter
@Entity(name="MeterMasterBean")@Table(name="re_meter_master")
public class MeterMasterBean implements MeterMasterInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    Long id;
    @Column(name="meterno")
    String meterno;
    @Column(name="make")
    String make;
    @Column(name="category")
    String category;
    @Column(name="type")
    String type;
    @Column(name="meter_class")
    String meter_class;
    @Column(name="meter_ctr")
    String meter_ctr;
    @Column(name="meter_ptr")
    String meter_ptr;
    @Column(name="me_ctr")
    String me_ctr;
    @Column(name="me_ptr")
    String me_ptr;

    @Column(name="dial_bmf")
    String dial_bmf;
    @Column(name="equip_class")
    String equip_class;
    @Column(name="phase")
    String phase;
    @Column(name="metergrp")
    String metergrp;
    @Column(name="mf")
    String mf;
    @Column(name="install_date")
    String install_date;
    @Column(name="created_by")
    String created_by;
    @Column(name="updated_by")
    String updated_by;
    @Column(name="created_on")
    Timestamp created_on;
    @Column(name="updated_on")
    Timestamp updated_on;
    @Column(name="status")
    String status;
    @Column(name="remark")
    String remark;


}
