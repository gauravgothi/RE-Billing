package in.co.mpwin.rebilling.interfaces.metermaster;

import in.co.mpwin.rebilling.interfaces.BeanInterface;
import jakarta.persistence.Column;

import java.sql.Timestamp;

public interface MeterMasterInterface extends BeanInterface {
    public Long getId();
    public void setId(Long id);
    public String getMeterNumber() ;
    public void setMeterNumber(String meterno);
    public String getMake();
    public void setMake(String make);
    public String getCategory();
    public void setCategory(String category);
    public String getType();
    public void setType(String type);
    public String getMeterClass();
    public void setMeterClass(String meter_class);
    public String getMeterCtr();
    public void setMeterCtr(String meter_ctr);
    public String getMeterPtr();
    public void setMeterPtr(String meter_ptr);
    public String getMeCtr();
    public void setMeCtr(String me_ctr);
    public String getMePtr();
    public void setMePtr(String me_ptr);
    public String getDialBmf();
    public void setDialBmf(String dial_bmf);
    public String getEquipClass();
    public void setEquipClass(String equip_class);
    public String getPhase();
    public void setPhase(String phase);
    public String getMeterGroup();
    public void setMeterGroup(String metergrp);
    public String getMf();
    public void setMf(String mf);
    public String getInstallDate();
    public void setInstallDate(String install_date);
    public String getCreatedBy();
    public void setCreatedBy(String created_by);
    public String getUpdatedBy();
    public void setUpdatedBy(String updated_by);
    public Timestamp getCreatedOn();
    public void setCreatedOn(Timestamp created_on);
    public Timestamp getUpdatedOn();
    public void setUpdatedOn(Timestamp updated_on);
    public String getStatus();
    public void setStatus(String status);
    public String getRemark();
    public void setRemark(String remark);
    public String getIsMapped();
    public void setIsMapped(String isMapped);
}
