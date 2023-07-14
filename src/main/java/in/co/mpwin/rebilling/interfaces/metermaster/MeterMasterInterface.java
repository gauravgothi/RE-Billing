package in.co.mpwin.rebilling.interfaces.metermaster;

import in.co.mpwin.rebilling.interfaces.BeanInterface;
import jakarta.persistence.Column;

import java.sql.Timestamp;

public interface MeterMasterInterface extends BeanInterface {
    public Long getId();
    public void setId(Long id);
    public String getMeterno() ;
    public void setMeterno(String meterno);
    public String getMake();
    public void setMake(String make);
    public String getCategory();
    public void setCategory(String category);
    public String getType();
    public void setType(String type);
    public String getMeter_class();
    public void setMeter_class(String meter_class);
    public String getMeter_ctr();
    public void setMeter_ctr(String meter_ctr);
    public String getMeter_ptr();
    public void setMeter_ptr(String meter_ptr);
    public String getMe_ctr();
    public void setMe_ctr(String me_ctr);
    public String getMe_ptr();
    public void setMe_ptr(String me_ptr);
    public String getDial_bmf();
    public void setDial_bmf(String dial_bmf);
    public String getEquip_class();
    public void setEquip_class(String equip_class);
    public String getPhase();
    public void setPhase(String phase);
    public String getMetergrp();
    public void setMetergrp(String metergrp);
    public String getMf();
    public void setMf(String mf);
    public String getInstall_date();
    public void setInstall_date(String install_date);
    public String getCreated_by();
    public void setCreated_by(String created_by);
    public String getUpdated_by();
    public void setUpdated_by(String updated_by);
    public Timestamp getCreated_on();
    public void setCreated_on(Timestamp created_on);
    public Timestamp getUpdated_on();
    public void setUpdated_on(Timestamp updated_on);
    public String getStatus();
    public void setStatus(String status);
    public String getRemark();
    public void setRemark(String remark);
}
