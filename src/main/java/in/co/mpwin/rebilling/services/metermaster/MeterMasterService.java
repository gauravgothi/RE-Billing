package in.co.mpwin.rebilling.services.metermaster;

import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import in.co.mpwin.rebilling.beans.metermaster.MeterMasterBean;
import in.co.mpwin.rebilling.dao.metermaster.MeterMasterDao;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.miscellanious.TokenInfo;
import in.co.mpwin.rebilling.repositories.metermaster.MeterMasterRepo;
import in.co.mpwin.rebilling.services.developermaster.DeveloperMasterService;
import in.co.mpwin.rebilling.services.mapping.MeterFeederPlantMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MeterMasterService {
    @Autowired private MeterMasterDao  meterMasterDao;

    @Autowired private MeterFeederPlantMappingService mfpService;

    @Autowired private  DeveloperMasterService developerMasterService;
    @Autowired private MeterMasterRepo meterMasterRepo;

    public MeterMasterBean getMeterDetailsByMeterNo(String meterno, String status) {
        MeterMasterBean meterMasterBean = new MeterMasterBean();
        try {
            meterMasterBean = meterMasterDao.getMeterDetailsByMeterNo(meterno,status);
        }
        catch (Exception e) {
            System.out.print(e);
            e.printStackTrace();
        }
        return meterMasterBean;
    }

    public ArrayList<MeterMasterBean> getAllMeterByStatus(String status) {
        ArrayList<MeterMasterBean> meterList = new ArrayList<MeterMasterBean>();
        try {
            meterList = meterMasterDao.getAllMeterByStatus(status);
        }
        catch (Exception e) {
            System.out.print(e);
            e.printStackTrace();
        }
        return meterList;
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
    )   {
        int resp_code = 0;
        try {
            resp_code = meterMasterDao.createMeterMaster(METERNO,
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
        return resp_code;
    }*/


    public MeterMasterBean createMeterMaster(MeterMasterBean meterMasterBean) {
        //int result = -1;
        MeterMasterBean mmb = new MeterMasterBean();
        try {
                mmb = meterMasterDao.createMeterMaster(meterMasterBean);
        }catch (ApiException apiException){
            throw apiException;
        }catch (DataIntegrityViolationException d){
            throw d;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        return mmb;
    }

    public  MeterMasterBean updateMeterStatusAndMappingByMeterNo(String meterNumber, String status, String isMapped) {
       MeterMasterBean meterBean = meterMasterRepo.findByMeterNumberAndStatus(meterNumber,status);
       meterBean.setIsMapped(isMapped);
       meterBean.setUpdatedBy(new TokenInfo().getCurrentUsername());
       meterBean.setUpdatedOn(new DateMethods().getServerTime());
       return meterMasterRepo.save(meterBean);
    }

    public List<MeterMasterBean> getMeterDetailsByCategory(String category, String status, String isMapped) {
        try {
            return meterMasterRepo.findByCategoryAndStatusAndIsMapped(category, status, isMapped);
        } catch (DataIntegrityViolationException ex) {
            throw ex;
        }
    }

    public List<Map<String,String>> getMetersByUser(){
        List<Map<String,String>> meterList = new ArrayList<>();
        try {
                String username = new TokenInfo().getCurrentUsername();
                String developerId = String.valueOf(developerMasterService.getDeveloperIdByUsername(username));
                List<MeterFeederPlantMappingBean> mfpBeans = mfpService.getMappingByDeveloperId(developerId,"active");
                List<String> meters = mfpBeans.stream().flatMap(m-> Stream.of(m.getMainMeterNo(),m.getCheckMeterNo())).distinct().collect(Collectors.toList());
                for (String meter : meters){
                    Map<String,String> m = new HashMap<>();
                    String meterCategory = meterMasterRepo.findByMeterNumberAndStatus(meter,"active").getCategory();
                    m.put("meterNo",meter);
                    m.put("meterCategory",meterCategory);
                    meterList.add(m);
            }
                return meterList;
        }catch (ApiException apiException){
            throw apiException;
        }catch (DataIntegrityViolationException d){
            throw d;
        }catch (Exception e){
            throw e;
        }
    }

    public List<Map<String,String>> getMeters(){
        List<Map<String,String>> meterList = new ArrayList<>();
        try {

            List<MeterMasterBean> meters = meterMasterRepo.findByStatusAndIsMapped("active","yes");
            for (MeterMasterBean bean : meters){
                Map<String,String> m = new HashMap<>();
                m.put("meterNo", bean.getMeterNumber());
                m.put("meterCategory", bean.getCategory());
                meterList.add(m);
            }
            return meterList;
        }catch (ApiException apiException){
            throw apiException;
        }catch (DataIntegrityViolationException d){
            throw d;
        }catch (Exception e){
            throw e;
        }
    }

    public List<MeterMasterBean> getMeterByStatusAndIsMappped(String status, String mapped) {
        try{
        List<MeterMasterBean> unmappedMeters =  meterMasterRepo.findByStatusAndIsMappedOrderById(status,mapped);
        if (unmappedMeters.isEmpty())
            throw new ApiException(HttpStatus.BAD_REQUEST,"new meter list are not found in meter master.");
        return unmappedMeters;
        } catch (ApiException apiException) {
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            throw d;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<MeterMasterBean> getUnmappedMeterBeans(String status, String isMapped, String category) {
        return meterMasterRepo.findByStatusAndIsMappedAndCategory(status,isMapped,category);

    }
}
