package in.co.mpwin.rebilling.services.metermaster;

import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import in.co.mpwin.rebilling.beans.metermaster.MeterMasterBean;
import in.co.mpwin.rebilling.beans.metermaster.MeterReplacementBean;
import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.dto.MeterReplacementDto;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.repositories.metermaster.MeterReplacementRepo;
import in.co.mpwin.rebilling.services.mapping.MeterFeederPlantMappingService;
import in.co.mpwin.rebilling.services.readingservice.MeterReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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




    @Transactional
    public String replaceMeter(MeterReplacementDto meterReplacementDto) {

        MeterMasterBean oldMeter = meterMasterService.getMeterDetailsByMeterNo(meterReplacementDto.getOldMeterNumber(), "active");
        MeterMasterBean newMeter = meterMasterService.getMeterDetailsByMeterNo(meterReplacementDto.getNewMeterNumber(), "active");

        if (oldMeter == null || newMeter == null) {
            return " old meter or new meter detail are not found in meter master data";
        }

        MeterReadingBean lastReadingBean = meterReadingService.GetLastReadingByMeterNoAndStatus(meterReplacementDto.getOldMeterNumber(), "active");
        if (lastReadingBean == null) {
            return "last meter reading not found for meter no. " + meterReplacementDto.getOldMeterNumber();
        }

        if ((meterReplacementDto.getOldMeterFR().compareTo(lastReadingBean.getEActiveEnergy()) < 0) &&
                (meterReplacementDto.getReplaceDate().compareTo(lastReadingBean.getReadingDate()) < 0)) {
            return " old meter final reading or replacement date is less than in last reading data";
        }

        MeterFeederPlantMappingBean newMFPMapping = new MeterFeederPlantMappingBean();

        MeterFeederPlantMappingBean oldMFPMapping = meterFeederPlantMappingService.getLastMFPMappingByMeterNo(oldMeter.getMeterNumber(), oldMeter.getType(), "active");
        if (oldMFPMapping == null) {
            return " mfp mapping not found for old meter number " + oldMeter.getMeterNumber();
        }
        Boolean mfpMapped = false;
        Boolean oldMeterUnmapped = false;
        Boolean newMeterMapped = false;
        Boolean meterReplacementHistory = false;

        mfpMapped = updateMFPMapping(oldMFPMapping, newMFPMapping, meterReplacementDto.getNewMeterNumber(), oldMeter.getType(), meterReplacementDto.getReplaceDate());
        oldMeterUnmapped = updateMeterStatusAndMappingByMeterNo(meterReplacementDto.getOldMeterNumber(), "active", "replaced");
        newMeterMapped = updateMeterStatusAndMappingByMeterNo(meterReplacementDto.getNewMeterNumber(), "active", "yes");
        meterReplacementHistory = meterReplacementTable(meterReplacementDto.getOldMeterNumber(), meterReplacementDto.getNewMeterNumber(), meterReplacementDto.getReplaceDate(),oldMFPMapping);

        if (mfpMapped && oldMeterUnmapped && newMeterMapped && meterReplacementHistory)
            return "meter replacement done.";
        else
            return "something went wrong.";

    }

    @Transactional
    public Boolean updateMFPMapping(MeterFeederPlantMappingBean oldMFPMapping, MeterFeederPlantMappingBean newMFPMapping, String newMeterNo, String category, Date replaceDate) {

        switch (category
        ) {
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


        newMFPMapping.setCreatedOn(replacementDate);
        newMFPMapping.setUpdatedOn(replacementDate);
        newMFPMapping.setCreatedBy("IT-Admin");
        newMFPMapping.setUpdatedBy("IT-Admin");
        newMFPMapping.setEndDate(oldMFPMapping.getEndDate());
        newMFPMapping.setDeveloperId(oldMFPMapping.getDeveloperId());
        newMFPMapping.setPlantCode(oldMFPMapping.getPlantCode());
        newMFPMapping.setFeederCode(oldMFPMapping.getFeederCode());
        newMFPMapping.setStatus("active");

        try {
            meterFeederPlantMappingService.updateMFPMapping(oldMFPMapping.getId(), replacementDate);
            newMFPMapping = meterFeederPlantMappingService.updateMFPMapping(newMFPMapping);
        } catch (DataIntegrityViolationException ex) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "unable to update MFP mapping status due to error" + ex.getMessage());
        }
        if (newMFPMapping != null)
            return true;
        else
            return false;
    }

    @Transactional
    private Boolean updateMeterStatusAndMappingByMeterNo(String meterNumber, String status, String isMapped) {
        try {
            meterMasterService.updateMeterStatusAndMappingByMeterNo(meterNumber, status, isMapped);
        } catch (DataIntegrityViolationException ex) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "unable to update meter status due to error" + ex.getMessage());
        }

        return true;
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

    @Transactional
    public String replaceMeterMethod(MeterReadingBean oldMeterBean, MeterReadingBean newMeterBean) {

        MeterMasterBean oldMeter = meterMasterService.getMeterDetailsByMeterNo(oldMeterBean.getMeterNo(), "active");
        MeterMasterBean newMeter = meterMasterService.getMeterDetailsByMeterNo(newMeterBean.getMeterNo(), "active");

        if (oldMeter == null || newMeter == null) {
            return " old meter or new meter detail are not found in meter master data";
        }
        MeterReadingBean lastReadingBean = meterReadingService.GetLastReadingByMeterNoAndStatus(oldMeterBean.getMeterNo(), "active");
        if (lastReadingBean==null) {
            return "last meter reading not found for meter no. " + oldMeterBean.getMeterNo();
        }
        if ((oldMeterBean.getEActiveEnergy().compareTo(lastReadingBean.getEActiveEnergy()) < 0) &&
                (oldMeterBean.getReadingDate().compareTo(lastReadingBean.getReadingDate()) < 0)) {
            return " old meter final reading or replacement date is less than in last reading data";
        }
        MeterFeederPlantMappingBean newMFPMapping = new MeterFeederPlantMappingBean();

        MeterFeederPlantMappingBean oldMFPMapping = meterFeederPlantMappingService.getLastMFPMappingByMeterNo(oldMeter.getMeterNumber(), oldMeter.getCategory(), "active");
        if (oldMFPMapping==null) {
            return " mfp mapping not found for old meter number " + oldMeter.getMeterNumber();
        }
        try {
            Boolean mfpMapped = false;
            Boolean oldMeterUnmapped = false;
            Boolean newMeterMapped = false;
            Boolean meterReplacementHistory = false;
            Boolean readingSRFR = false;

            mfpMapped = updateMFPMapping(oldMFPMapping, newMFPMapping, newMeterBean.getMeterNo(), oldMeter.getCategory(), newMeterBean.getReadingDate());
            newMeterMapped = updateMeterStatusAndMappingByMeterNo(newMeterBean.getMeterNo(), "active", "yes");
            meterReplacementHistory = meterReplacementTable(oldMeterBean.getMeterNo(), newMeterBean.getMeterNo(), oldMeterBean.getReadingDate(), oldMFPMapping);
            readingSRFR = insertReadingForSRFR(oldMeterBean, newMeterBean);
            oldMeterUnmapped = updateMeterStatusAndMappingByMeterNo(oldMeterBean.getMeterNo(), "active", "replaced");
            if (mfpMapped && oldMeterUnmapped && newMeterMapped && meterReplacementHistory && readingSRFR)
                return "meter replacement done.";
            else
                return "something went wrong.";
        }catch (ApiException apiException){
            throw apiException;
        }  catch (DataIntegrityViolationException d) {
            throw d;
        } catch (Exception e) {
            throw e;
        }


    }
    @Transactional
    private Boolean insertReadingForSRFR(MeterReadingBean oldMeterBean, MeterReadingBean newMeterBean) {

        oldMeterBean.setReadingType("FR");
        oldMeterBean.setReadSource("web");
        oldMeterBean.setCurrentState("initial_read");

        newMeterBean.setReadingType("SR");
        newMeterBean.setReadSource("web");
        newMeterBean.setCurrentState("initial_read");
        MeterReadingBean fr =  meterReadingService.createMeterReading(oldMeterBean);
        MeterReadingBean sr =  meterReadingService.createMeterReading(newMeterBean);
        if(fr!=null && sr!=null)
            return true;
        else
            return false;
    }

    public List<MeterReplacementBean> getMeterReplacementList(String status) {

      return meterReplacementRepo.findAllByStatus(status);

    }
}
