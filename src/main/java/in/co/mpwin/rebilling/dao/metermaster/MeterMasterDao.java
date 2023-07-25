package in.co.mpwin.rebilling.dao.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterMasterBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.miscellanious.TokenInfo;
import in.co.mpwin.rebilling.miscellanious.ValidatorService;
import in.co.mpwin.rebilling.repositories.metermaster.MeterMasterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class MeterMasterDao {
    @Autowired
    MeterMasterRepo meterMasterRepo;
    public MeterMasterBean getMeterDetailsByMeterNo(String meterno, String status) {
        MeterMasterBean meterMasterBean = new MeterMasterBean();
        try {
                meterMasterBean = meterMasterRepo.findByMeterNumberAndStatus(meterno,status);
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

            meterList = meterMasterRepo.findAllByStatus(status);
        }
        catch (Exception e) {
            System.out.print(e);
            e.printStackTrace();
        }
        return meterList;
    }

    public MeterMasterBean createMeterMaster(MeterMasterBean meterMasterBean){
        //int result = -1;
        MeterMasterBean mmb = new MeterMasterBean();
        try {

           /* meterMasterBean.setCreatedOn(new DateMethods().getServerTime());
            meterMasterBean.setUpdatedOn(new DateMethods().getServerTime());
            meterMasterBean.setCreatedBy(new TokenInfo().getCurrentUsername());
            meterMasterBean.setUpdatedBy(new TokenInfo().getCurrentUsername());
            meterMasterBean.setStatus("active");
            meterMasterBean.setRemark("NA");*/

            //To check the duplicate meter-make-status combination
//            boolean isExist=meterMasterRepo.existsByMeterNumberAndMakeAndStatus(meterMasterBean.getMeterNumber(),
//                                                    meterMasterBean.getMake(),meterMasterBean.getStatus());

            //To check the duplicate meter-make-status combination
            boolean isExist = false;
            List<MeterMasterBean> meterMasterBeanList = meterMasterRepo.findByMeterNumberAndMakeAndStatus(meterMasterBean.getMeterNumber(),
                    meterMasterBean.getMake(),meterMasterBean.getStatus());
            if(meterMasterBeanList.size()>0) {
                isExist = true;
                System.out.println("isExist : " + isExist);
                return null;
            }
            //Set the Audit control parameters, Globally
            new AuditControlServices().setInitialAuditControlParameters(meterMasterBean);
            //Validate the meterno remove the space.
            meterMasterBean.setMeterNumber(new ValidatorService().removeSpaceFromString(meterMasterBean.getMeterNumber()));

            mmb = meterMasterRepo.save(meterMasterBean);
        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
        return mmb;
    }
    
    /*public int createMeterMaster(
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
    }*/
    
//DIAL MF calculation : var mf = ((getMeCtr()*getMePtr())/(getMtCtr()*getMtPtr()))*getDialBmf();
}
