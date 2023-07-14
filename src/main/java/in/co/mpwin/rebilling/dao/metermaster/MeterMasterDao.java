package in.co.mpwin.rebilling.dao.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterMasterBean;
import in.co.mpwin.rebilling.repositories.metermaster.MeterMasterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

@Service
public class MeterMasterDao {
    @Autowired
    MeterMasterRepo meterMasterRepo;
    public MeterMasterBean getMeterDetailsByMeterNo(String meterno, String status) {
        MeterMasterBean meterMasterBean = new MeterMasterBean();
        try {
                meterMasterBean = meterMasterRepo.getMeterDetailsByMeterNo(meterno,status);
        }
        catch (Exception e) {
            System.out.print(e);
            e.printStackTrace();
        }
        return meterMasterBean;
    }

    public ArrayList<MeterMasterBean> getAllMeterByStatus (String status) {
        ArrayList<MeterMasterBean> meterList = new ArrayList<MeterMasterBean>();
        try {

            meterList = meterMasterRepo.getAllMeterByStatus(status);
        }
        catch (Exception e) {
            System.out.print(e);
            e.printStackTrace();
        }
        return meterList;
    }
    
    public int createMeterMaster(
            String METERNO,
            String MAKE,
            String CATEGORY,
            String TYPE,
            String METER_CLASS,
            String METER_CTR,
            String METER_PTR,
            String ME_CTR,
            String ME_PTR,
            String DIAL_BMF,
            String EQUIP_CLASS,
            String PHASE,
            String METERGRP,
            String MF,
            Date install_date,
            String created_by,
            String updated_by,
            Timestamp created_on,
            Timestamp updated_on,
            String status,
            String remark
            ) {
        int result = 0;
        try {
           result = meterMasterRepo.createMeterMaster(METERNO,
                    MAKE,
                    CATEGORY,
                    TYPE,
                    METER_CLASS,
                    METER_CTR,
                    METER_PTR,
                    ME_CTR,
                    ME_PTR,
                    DIAL_BMF,
                    EQUIP_CLASS,
                    PHASE,
                    METERGRP,
                    MF,
                    install_date,
                    created_by,
                    updated_by,
                    created_on,
                    updated_on,
                    status,
                    remark);
        }
        catch (Exception e) {
            System.out.print(e);
            e.printStackTrace();
        }
        return result;
    }

    public int createMeterMaster2(MeterMasterBean meterMasterBean){
        int result = -1;
        try {
            result = meterMasterRepo.createMeterMaster2(meterMasterBean);
        }catch (Exception e) {
            System.out.print(e);
            e.printStackTrace();
        }
        return result;
    }
    

}
