package in.co.mpwin.rebilling.services.mapping;

import in.co.mpwin.rebilling.beans.developermaster.DeveloperMasterBean;
import in.co.mpwin.rebilling.beans.feedermaster.FeederMasterBean;
import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import in.co.mpwin.rebilling.beans.metermaster.MeterMasterBean;
import in.co.mpwin.rebilling.beans.plantmaster.PlantMasterBean;
import in.co.mpwin.rebilling.beans.thirdparty.DeveloperPlantDto;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.miscellanious.TokenInfo;
import in.co.mpwin.rebilling.miscellanious.ValidatorService;
import in.co.mpwin.rebilling.repositories.mapping.MeterFeederPlantMappingRepo;
import in.co.mpwin.rebilling.repositories.metermaster.MeterMasterRepo;
import in.co.mpwin.rebilling.services.developermaster.DeveloperMasterService;
import in.co.mpwin.rebilling.services.feedermaster.FeederMasterService;
import in.co.mpwin.rebilling.services.machinemaster.MachineMasterService;
import in.co.mpwin.rebilling.services.metermaster.MeterMasterService;
import in.co.mpwin.rebilling.services.plantmaster.PlantMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@Service
public class MeterFeederPlantMappingService {

    @Autowired private MeterFeederPlantMappingRepo meterFeederPlantMappingRepo;
    @Autowired private DeveloperMasterService developerMasterService;
    @Autowired private PlantMasterService plantMasterService;
    @Autowired private FeederMasterService feederMasterService;
    @Autowired private MachineMasterService machineMasterService;

    @Autowired private MeterMasterRepo meterMasterRepo;


    @Transactional
    public MeterFeederPlantMappingBean createNewMapping(MeterFeederPlantMappingBean meterFeederPlantMappingBean) throws ParseException {

        try {

            meterFeederPlantMappingBean.setMainMeterNo(new ValidatorService().removeSpaceFromString(meterFeederPlantMappingBean.getMainMeterNo()));
            meterFeederPlantMappingBean.setCheckMeterNo(new ValidatorService().removeSpaceFromString(meterFeederPlantMappingBean.getCheckMeterNo()));
            meterFeederPlantMappingBean.setStandbyMeterNo(new ValidatorService().removeSpaceFromString(meterFeederPlantMappingBean.getStandbyMeterNo()));
            meterFeederPlantMappingBean.setFeederCode(new ValidatorService().removeSpaceFromString(meterFeederPlantMappingBean.getFeederCode()));
            meterFeederPlantMappingBean.setPlantCode(new ValidatorService().removeSpaceFromString(meterFeederPlantMappingBean.getPlantCode()));
            meterFeederPlantMappingBean.setDeveloperId(new ValidatorService().removeSpaceFromString(meterFeederPlantMappingBean.getDeveloperId()));

            //check for existence of mapping if already exist with same mapping then throw api exception
            MeterFeederPlantMappingBean temp = meterFeederPlantMappingRepo.findByMainMeterNoCheckMeterNoStandbyMeterNoAndDeveloperId(meterFeederPlantMappingBean.getMainMeterNo(),meterFeederPlantMappingBean.getCheckMeterNo(),
                    meterFeederPlantMappingBean.getStandbyMeterNo(),meterFeederPlantMappingBean.getDeveloperId());
            if(temp!=null) {
                throw new ApiException(HttpStatus.BAD_REQUEST,"developer, plant, feeder and meters mapping is already exit.");
            }
            //Set the Audit control parameters, Globally
            new AuditControlServices().setInitialAuditControlParameters(meterFeederPlantMappingBean);
            // set end date of mapping is future date "2024-12-31". this end_date is used to end the mapping due to meter replacement.
            Date futureEndDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-12-31");
            meterFeederPlantMappingBean.setEndDate(futureEndDate);
            //save mfp mapping
            MeterFeederPlantMappingBean mfpm = meterFeederPlantMappingRepo.save(meterFeederPlantMappingBean);
            //set main , check and standby meter is_mapped=yes
            MeterMasterBean mainMeterBean, checkMeterBean ,standbyMeterBean;
           if(mfpm!=null) {
               // set mapped yes to main meter
               mainMeterBean = meterMasterRepo.findByMeterNumberAndStatus(mfpm.getMainMeterNo(), "active");
               if(mainMeterBean==null)
                   throw new ApiException(HttpStatus.BAD_REQUEST,"main meter no. "+mfpm.getMainMeterNo()+" is not found in meter master in active status.");
               mainMeterBean.setIsMapped("yes");
               mainMeterBean.setUpdatedBy(new TokenInfo().getCurrentUsername());
               mainMeterBean.setUpdatedOn(new DateMethods().getServerTime());
               meterMasterRepo.save(mainMeterBean);

               // set mapped yes to check meter
               checkMeterBean = meterMasterRepo.findByMeterNumberAndStatus(mfpm.getCheckMeterNo(),"active");
               if(checkMeterBean==null)
                   throw new ApiException(HttpStatus.BAD_REQUEST,"check meter no. "+mfpm.getCheckMeterNo()+" is not found in meter master in active status.");
                   checkMeterBean.setIsMapped("yes");
                   checkMeterBean.setUpdatedBy(new TokenInfo().getCurrentUsername());
                   checkMeterBean.setUpdatedOn(new DateMethods().getServerTime());
                   meterMasterRepo.save(checkMeterBean);

               // set mapped yes to standby meter if standby meter no is present in mfp mapping
               standbyMeterBean = meterMasterRepo.findByMeterNumberAndStatus(mfpm.getStandbyMeterNo(),"active");
               if(standbyMeterBean!=null)
                   if(mfpm.getStandbyMeterNo().equals(standbyMeterBean.getMeterNumber()))
                   {
                       standbyMeterBean.setIsMapped("yes");
                       standbyMeterBean.setUpdatedBy(new TokenInfo().getCurrentUsername());
                       standbyMeterBean.setUpdatedOn(new DateMethods().getServerTime());
                       meterMasterRepo.save(checkMeterBean);
                   }
               }
            return mfpm;
        }catch(ApiException apiException) {
            throw apiException;
        } catch(DataIntegrityViolationException d) {
            throw d;
        } catch (NullPointerException ex)
        {
            throw ex;
        } catch(Exception e) {
            throw e;
        }
    }

    public List<MeterFeederPlantMappingBean> getAllMapping(String status) {

        List<MeterFeederPlantMappingBean> allMappingList;
        try {
            allMappingList=(List<MeterFeederPlantMappingBean>) meterFeederPlantMappingRepo.findByStatus(status);
        } catch (Exception e) {
            throw e;
        }
        return allMappingList;
    }

    public MeterFeederPlantMappingBean getMappingById(Long id, String status) {

        MeterFeederPlantMappingBean mappingBean =null ;
        try{
            mappingBean= meterFeederPlantMappingRepo.findByIdAndStatus(id,status);
        }catch (Exception e){
            throw e;
        }
        return mappingBean;
    }

    public List<MeterFeederPlantMappingBean> getMappingByMainMeterNo(String mmn, String status) {
        List<MeterFeederPlantMappingBean> mappingBean = new ArrayList<>();
        try{
            mappingBean= meterFeederPlantMappingRepo.findByMainMeterNoAndStatus(mmn,status);
        }catch(Exception e){
           throw e;
        }
        return mappingBean;

    }

    public List<MeterFeederPlantMappingBean> getMappingByCheckMeterNo(String mmn, String status) {
        List<MeterFeederPlantMappingBean> mappingBean = new ArrayList<>();
        try{
            mappingBean= meterFeederPlantMappingRepo.findByCheckMeterNoAndStatus(mmn,status);
        }catch (Exception e){
           throw e;
        }
        return mappingBean;

    }

    public List<MeterFeederPlantMappingBean> getMappingByStandbyMeterNo(String smn, String status) {

        List<MeterFeederPlantMappingBean> mappingBean = new ArrayList<>();
        try{
            mappingBean= meterFeederPlantMappingRepo.findByStandbyMeterNoAndStatus(smn,status);
        }catch (Exception e){
            throw e;
        }
        return mappingBean;
    }

    public MeterFeederPlantMappingBean getByAnyMeterNoAndStatus(String meterNo, String status) {

        MeterFeederPlantMappingBean mappingBean=null;
        try{
            mappingBean= meterFeederPlantMappingRepo.findByAnyMeterNoAndStatus(meterNo,status);
            if (mappingBean==null)
                throw new ApiException(HttpStatus.BAD_REQUEST,"No active mapping of Plant found for given meter..");
        }catch (ApiException apiException){
            throw apiException;
        }catch (DataIntegrityViolationException d){
            throw d;
        }catch (Exception e){
            throw e;
        }
        return mappingBean;
    }

    public List<MeterFeederPlantMappingBean> getMappingByDeveloperId(String di, String status) {
        List<MeterFeederPlantMappingBean> mappingBean = new ArrayList<>();
        try{
            mappingBean= meterFeederPlantMappingRepo.findByDeveloperIdAndStatus(di,status);
        }catch (Exception e){
           throw e;
        }
        return mappingBean;

    }

    public List<MeterFeederPlantMappingBean> getMappingByFeederCode(String fcode, String status) {

        List<MeterFeederPlantMappingBean> mappingBean = new ArrayList<>();
        try{
            mappingBean= meterFeederPlantMappingRepo.findByFeederCodeAndStatus(fcode,status);
        }catch (Exception e){
           throw e;
        }
        return mappingBean;
    }

    public List<MeterFeederPlantMappingBean> getMappingByPlantCode(String plantCode, String status) {

        List<MeterFeederPlantMappingBean> mappingBean = new ArrayList<>();
        try{
            mappingBean= meterFeederPlantMappingRepo.findByPlantCodeAndStatus(plantCode,status);
        }catch (Exception e){
           throw e;
        }
        return mappingBean;
    }

    public MeterFeederPlantMappingBean getLastMFPMappingByMeterNo(String meterNumber, String category, String status) {

        MeterFeederPlantMappingBean mfpMapping = null;
        switch(category)
        {
            case "MAIN":
                System.out.println("switch case : main to find last mapping by main meter");
                mfpMapping = meterFeederPlantMappingRepo.findLastMFPMappingByMainMeterNo(meterNumber,status);
                break;
            case "CHECK":
                mfpMapping =   meterFeederPlantMappingRepo.findLastMFPMappingByCheckMeterNo(meterNumber,status);
                break;
            case "STANDBY":
                mfpMapping =  meterFeederPlantMappingRepo.findLastMFPMappingByStandbyMeterNo(meterNumber,status);
                break;
            default:
                break;
        }
     return mfpMapping;
    }

    public void updateMFPMapping(Long id, Date replaceDate) {
        meterFeederPlantMappingRepo.updateMappingEndDatebyId(id,replaceDate);
    }

    public MeterFeederPlantMappingBean updateMFPMapping(MeterFeederPlantMappingBean newMFPMapping) {
           return meterFeederPlantMappingRepo.save(newMFPMapping);
    }


        public List<String> getDistinctPlantCodeByDeveloperId (String developerId, String status){
            List<String> plants = new ArrayList<>();
            try {
                plants = meterFeederPlantMappingRepo.findDistinctPlantCodeByDeveloperIdAndStatus(developerId, status);
                if(plants.size()==0)
                    throw new ApiException(HttpStatus.BAD_REQUEST,"Developer "+developerId +" not have any plant mapping");
            } catch (ApiException apiException){
                throw apiException;
            }
            catch (Exception e) {
                throw e;
            }
            return plants;
        }

        public List<MeterFeederPlantMappingBean> getMappingByDeveloperIdOrderByEndDate (String di, String status){
            List<MeterFeederPlantMappingBean> mappingBean = new ArrayList<>();
            try {
                mappingBean = meterFeederPlantMappingRepo.findAllByDeveloperIdAndStatusOrderByEndDateAsc(di, status);
                if (mappingBean.size() == 0)
                    throw new ApiException(HttpStatus.BAD_REQUEST,"Developer "+ di+" is not mapped to any plant..");
            } catch (ApiException apiException) {
                apiException.printStackTrace();
                System.out.println(apiException.getMessage());
            }
            catch (Exception e) {
                throw e;
            }
            return mappingBean;


        }

    public List<String> findMappedMeterListByEndDate(LocalDate endDate) {
        List<MeterFeederPlantMappingBean> mfpMapping =  meterFeederPlantMappingRepo.findMappedMeterListByEndDate(endDate,"active");
        if(mfpMapping.isEmpty())
            return null;
        List<String> meterList =new ArrayList<>();
        for(MeterFeederPlantMappingBean row : mfpMapping)
        {
            meterList.add(row.getMainMeterNo());
            meterList.add(row.getCheckMeterNo());
        }
        return meterList;
    }


}