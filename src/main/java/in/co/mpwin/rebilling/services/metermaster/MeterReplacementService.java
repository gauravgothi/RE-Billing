package in.co.mpwin.rebilling.services.metermaster;

import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import in.co.mpwin.rebilling.beans.metermaster.MeterMasterBean;
import in.co.mpwin.rebilling.beans.metermaster.MeterReplacementBean;
import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.dto.MeterReplacementDto;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.miscellanious.TokenInfo;
import in.co.mpwin.rebilling.repositories.mapping.MeterFeederPlantMappingRepo;
import in.co.mpwin.rebilling.repositories.metermaster.MeterMasterRepo;
import in.co.mpwin.rebilling.repositories.metermaster.MeterReplacementRepo;
import in.co.mpwin.rebilling.services.mapping.MeterFeederPlantMappingService;
import in.co.mpwin.rebilling.services.readingservice.MeterReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Service
public class MeterReplacementService {
    @Autowired
    MeterReplacementRepo meterReplacementRepo;
    @Autowired
    MeterMasterService meterMasterService;
    @Autowired
    MeterReadingService meterReadingService;

    @Autowired
    MeterFeederPlantMappingService meterFeederPlantMappingService;

    @Autowired
    AuditControlServices auditControlServices;
    @Autowired MeterMasterRepo meterMasterRepo;
    @Autowired private MeterFeederPlantMappingRepo meterFeederPlantMappingRepo;


    @Transactional
    public Boolean updateMFPMapping(MeterFeederPlantMappingBean oldMFPMapping, MeterFeederPlantMappingBean newMFPMapping, String newMeterNo, String category, Date replaceDate) {
        try {
        switch (category) {
            case "MAIN":
                newMFPMapping.setMainMeterNo(newMeterNo);
                newMFPMapping.setCheckMeterNo(oldMFPMapping.getCheckMeterNo());
                newMFPMapping.setStandbyMeterNo(oldMFPMapping.getStandbyMeterNo());
                break;
            case "CHECK":
                newMFPMapping.setCheckMeterNo(newMeterNo);
                newMFPMapping.setMainMeterNo(oldMFPMapping.getMainMeterNo());
                newMFPMapping.setStandbyMeterNo(oldMFPMapping.getStandbyMeterNo());
                break;
            case "STANDBY":
                newMFPMapping.setStandbyMeterNo(newMeterNo);
                newMFPMapping.setMainMeterNo(oldMFPMapping.getMainMeterNo());
                newMFPMapping.setCheckMeterNo(oldMFPMapping.getCheckMeterNo());
                break;
            default:
                break;
        }

        Timestamp replacementDate = new DateMethods().getServerTime();

        newMFPMapping.setCreatedOn(new DateMethods().getServerTime());
        newMFPMapping.setUpdatedOn(new DateMethods().getServerTime());
        newMFPMapping.setCreatedBy(new TokenInfo().getCurrentUsername());
        newMFPMapping.setUpdatedBy(new TokenInfo().getCurrentUsername());
        newMFPMapping.setEndDate(oldMFPMapping.getEndDate());
        newMFPMapping.setDeveloperId(oldMFPMapping.getDeveloperId());
        newMFPMapping.setPlantCode(oldMFPMapping.getPlantCode());
        newMFPMapping.setFeederCode(oldMFPMapping.getFeederCode());
        newMFPMapping.setStatus("active");

        // update old mfp mapping end date
        oldMFPMapping.setEndDate(new DateMethods().getServerTime());
        oldMFPMapping.setUpdatedOn(replacementDate);
        oldMFPMapping.setUpdatedBy(new TokenInfo().getCurrentUsername());


            MeterFeederPlantMappingBean resp1 =   meterFeederPlantMappingRepo.save(newMFPMapping);
            MeterFeederPlantMappingBean resp2 =    meterFeederPlantMappingRepo.save(oldMFPMapping);
            if (newMFPMapping != null)
                return true;
            else
                return false;

            }catch (DataIntegrityViolationException ex) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "unable to update MFP mapping status due to exception" + ex.getMessage().substring(0,250));
        }catch (NullPointerException ex){
            throw ex;
        }

    }

    @Transactional
    private Boolean updateMeterStatusAndMappingByMeterNo(String meterNumber, String status, String isMapped) {
        try {
           MeterMasterBean result =meterMasterService.updateMeterStatusAndMappingByMeterNo(meterNumber, status, isMapped);
           if(result!=null)
               return true;
        } catch (DataIntegrityViolationException ex) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "unable to update meter status due to exception" + ex.getMessage().substring(0,250));
        }
        return false;
    }

    @Transactional
    private Boolean meterReplacementTable(String oldMeterNumber, String newMeterNumber, Date replaceDate, MeterFeederPlantMappingBean oldMFPMapping) {
        MeterReplacementBean meterReplacementBean = new MeterReplacementBean();
        meterReplacementBean.setOldMeterNumber(oldMeterNumber);
        meterReplacementBean.setNewMeterNumber(newMeterNumber);
        meterReplacementBean.setReplaceDate(replaceDate);
        meterReplacementBean.setDeveloperId(oldMFPMapping.getDeveloperId());
        meterReplacementBean.setPlantCode(oldMFPMapping.getPlantCode());
        meterReplacementBean.setFeederCode(oldMFPMapping.getFeederCode());

        auditControlServices.setInitialAuditControlParameters(meterReplacementBean);
        try {
            MeterReplacementBean resp = meterReplacementRepo.save(meterReplacementBean);
            if (resp != null)
                return true;
        } catch (DataIntegrityViolationException ex) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "unable to save meter replacement history. " + ex.getMessage());
        }
        return false;
    }
   // table used for meter replacement are : 1. ecell.re_meter_feeder_plant_mapping for end old mapping by end date and create new mapping
   //2. ecell.re_meter_master for update old meter mapped=repalced
    @Transactional
    public Boolean replaceMeterMethod(MeterReadingBean oldMeterBean, MeterReadingBean newMeterBean) {
        try {
        MeterMasterBean oldMeter = meterMasterService.getMeterDetailsByMeterNo(oldMeterBean.getMeterNo(), "active");
        MeterMasterBean newMeter = meterMasterService.getMeterDetailsByMeterNo(newMeterBean.getMeterNo(), "active");

        if (oldMeter == null || newMeter == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST,"old meter or new meter detail are not found in meter master data.");
        }
        MeterReadingBean lastReadingBean = meterReadingService.GetLastReadingByMeterNoAndStatus(oldMeterBean.getMeterNo(), "active");
        if (lastReadingBean==null)
            throw new ApiException(HttpStatus.BAD_REQUEST,"Previous reading not found for old meter no. " + oldMeterBean.getMeterNo());

        if (oldMeterBean.getEActiveEnergy().compareTo(lastReadingBean.getEActiveEnergy()) < 0)
            throw new ApiException(HttpStatus.BAD_REQUEST,"Old meter final reading should not be less than last reading.");

        if(oldMeterBean.getReadingDate().compareTo(lastReadingBean.getReadingDate()) < 0)
            throw new ApiException(HttpStatus.BAD_REQUEST,"Meter replacement date should not be less than last reading date.");

        MeterFeederPlantMappingBean newMFPMapping = new MeterFeederPlantMappingBean();

        MeterFeederPlantMappingBean oldMFPMapping = meterFeederPlantMappingService.getLastMFPMappingByMeterNo(oldMeter.getMeterNumber(), oldMeter.getCategory(), "active");
        if (oldMFPMapping==null) {
            throw new ApiException(HttpStatus.BAD_REQUEST,"mfp mapping not found for old meter number." + oldMeter.getMeterNumber());
        }

            Boolean mfpMapped = false;
            Boolean oldMeterUnmapped = false;
            Boolean newMeterMapped = false;
            Boolean meterReplacementHistory = false;
            Boolean readingSRFR = false;
            // for meter replacement update mfp mapping.
            mfpMapped = updateMFPMapping(oldMFPMapping, newMFPMapping, newMeterBean.getMeterNo(), oldMeter.getCategory(), newMeterBean.getReadingDate());
            // change new meter mapped status from no to yes.
            newMeterMapped = updateMeterStatusAndMappingByMeterNo(newMeterBean.getMeterNo(), "active", "yes");
            // insert old and new meter number and date in replacement table for history
            meterReplacementHistory = meterReplacementTable(oldMeterBean.getMeterNo(), newMeterBean.getMeterNo(), oldMeterBean.getReadingDate(), oldMFPMapping);
            //insert SR and FR reading in reading table
            readingSRFR = insertReadingForSRFR(oldMeterBean, newMeterBean);
            // change old meter mapped status from yes to replaced.
            oldMeterUnmapped = updateMeterStatusAndMappingByMeterNo(oldMeterBean.getMeterNo(), "active", "replaced");
           // if meter replacement task successfully done than return true else return false.
            if (mfpMapped && oldMeterUnmapped && newMeterMapped && meterReplacementHistory && readingSRFR)
                return true;
            else
                return false;
        }catch (ApiException apiException){
            throw apiException;
        }  catch (DataIntegrityViolationException d) {
            throw d;
        } catch (NullPointerException e) {
            throw e;
        }catch (Exception e) {
            throw e;
        }

    }
    @Transactional
    private Boolean insertReadingForSRFR(MeterReadingBean oldMeterReadingBean, MeterReadingBean newMeterReadingBean) {

        oldMeterReadingBean.setReadingType("FR");
        oldMeterReadingBean.setReadSource("web");
        oldMeterReadingBean.setCurrentState("initial_read");
        oldMeterReadingBean.setEndDate(new DateMethods().getOneDayBefore(oldMeterReadingBean.getReadingDate()));
        oldMeterReadingBean.setMf(BigDecimal.valueOf(0));
        oldMeterReadingBean.setCreatedOn(new DateMethods().getServerTime());
        oldMeterReadingBean.setUpdatedOn(new DateMethods().getServerTime());
        oldMeterReadingBean.setCreatedBy(new TokenInfo().getCurrentUsername());
        oldMeterReadingBean.setUpdatedBy(new TokenInfo().getCurrentUsername());



        newMeterReadingBean.setReadingType("SR");
        newMeterReadingBean.setReadSource("web");
        newMeterReadingBean.setCurrentState("ht_accept");
        newMeterReadingBean.setMf(BigDecimal.valueOf(0));
        newMeterReadingBean.setStatus("active");
        newMeterReadingBean.setEndDate(new DateMethods().getOneDayBefore(newMeterReadingBean.getReadingDate()));
        newMeterReadingBean.setCreatedOn(new DateMethods().getServerTime());
        newMeterReadingBean.setUpdatedOn(new DateMethods().getServerTime());
        newMeterReadingBean.setCreatedBy(new TokenInfo().getCurrentUsername());
        newMeterReadingBean.setUpdatedBy(new TokenInfo().getCurrentUsername());

        MeterReadingBean fr =  meterReadingService.createMeterReading(oldMeterReadingBean);
        MeterReadingBean sr =  meterReadingService.createMeterReading(newMeterReadingBean);
        if(fr!=null && sr!=null)
            return true;
        else
            return false;
    }

    public List<MeterReplacementBean> getMeterReplacementList(String status) {

      return meterReplacementRepo.findAllByStatus(status);

    }

    public List<MeterMasterBean> getMappedMeterBeansByMfpMappingBean() {
        LocalDate endDate = LocalDate.now();
        try {
            List<String> mappedMeters = meterFeederPlantMappingService.findMappedMeterListByEndDate(endDate);
            if (mappedMeters.isEmpty())
                throw new ApiException(HttpStatus.BAD_REQUEST, "mapped meter list are not found in mfp mapping table");
            List<MeterMasterBean> meterList = meterMasterRepo.findByMeterNumberInAndStatusAndIsMapped(mappedMeters, "active", "yes");
            if (meterList.isEmpty())
                throw new ApiException(HttpStatus.BAD_REQUEST, "mapped meter list are not found in meter master");
            return meterList;
        } catch (ApiException apiException) {
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            throw d;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MeterMasterBean getMappedMeterBeanForReplacement(String meterNo) {
        LocalDate endDate = LocalDate.now();
        MeterFeederPlantMappingBean meterMfpMapping = null;
        try {
        MeterMasterBean oldMeter = meterMasterRepo.findByMeterNumberAndStatusAndIsMapped(meterNo, "active", "yes");
        if(oldMeter==null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "this meter number is not found in meter master with active and mapped condition.");
        if(oldMeter.getCategory().equals("MAIN"))
            meterMfpMapping = meterFeederPlantMappingRepo.findByMainMeterNoAndEndDateAndStatus(oldMeter.getMeterNumber(),endDate,"active");
        if(oldMeter.getCategory().equals("CHECK"))
            meterMfpMapping =   meterFeederPlantMappingRepo.findByCheckMeterNoAndEndDateAndStatus(oldMeter.getMeterNumber(),endDate,"active");
        if(oldMeter.getCategory().equals("STANDBY"))
            meterMfpMapping =   meterFeederPlantMappingRepo.findByStandbyMeterNoAndEndDateAndStatus(oldMeter.getMeterNumber(),endDate,"active");
        if(meterMfpMapping==null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "this meter number is not mapped with feeder, plant and developer in mfp mapping table.");
        return oldMeter;
        } catch (ApiException apiException) {
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            throw d;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
