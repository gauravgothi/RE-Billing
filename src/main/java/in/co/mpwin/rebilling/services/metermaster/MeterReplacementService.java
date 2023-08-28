package in.co.mpwin.rebilling.services.metermaster;

import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import in.co.mpwin.rebilling.beans.metermaster.MeterMasterBean;
import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.dto.MeterReplacementDto;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.services.mapping.MeterFeederPlantMappingService;
import in.co.mpwin.rebilling.services.readingservice.MeterReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;



@Service
public class MeterReplacementService {

    @Autowired
    MeterMasterService meterMasterService;
    @Autowired
    MeterReadingService meterReadingService;

    @Autowired
    MeterFeederPlantMappingService meterFeederPlantMappingService;

    public String replaceMeter(MeterReplacementDto meterReplacementDto)
    {

        MeterMasterBean oldMeter = meterMasterService.getMeterDetailsByMeterNo(meterReplacementDto.getOldMeterNumber(),"active");
        MeterMasterBean newMeter = meterMasterService.getMeterDetailsByMeterNo(meterReplacementDto.getNewMeterNumber(),"active");

        if(oldMeter == null || newMeter == null)
        {
            return " old meter or new meter detail are not found in meter master data";
        }
        System.out.println("meter found in meter master old mtr ="+oldMeter.getMeterNumber()+"new meter ="+newMeter.getMeterNumber());


        MeterReadingBean lastReadingBean =meterReadingService.GetLastReadingByMeterNoAndStatus(meterReplacementDto.getOldMeterNumber(),"active");
        System.out.println("old meter read and date ="+lastReadingBean.getEActiveEnergy()+"read date="+lastReadingBean.getReadingDate());
        if((meterReplacementDto.getOldMeterFR().compareTo(lastReadingBean.getEActiveEnergy())<0)&&
                (meterReplacementDto.getReplaceDate().compareTo(lastReadingBean.getReadingDate())<0)){
            return " old meter final reading or replacement date is less than in last reading data";
        }

        MeterFeederPlantMappingBean newMFPMapping = new MeterFeederPlantMappingBean();

        MeterFeederPlantMappingBean oldMFPMapping  =  meterFeederPlantMappingService.getLastMFPMappingByMeterNo(oldMeter.getMeterNumber(),oldMeter.getType(),"active");
        if(oldMFPMapping==null)
            return " mfp mapping not found for this old meter number.";

        System.out.println("old mfp mapping bean = "+oldMFPMapping.toString());
        Boolean mfpMapped = updateMFPMapping(oldMFPMapping,newMFPMapping,meterReplacementDto.getNewMeterNumber(), oldMeter.getType(), meterReplacementDto.getReplaceDate());
        Boolean meterUnmapped = updateMeterStatusAndMappingByMeterNo(meterReplacementDto.getOldMeterNumber(),"inactive", "replaced");
        if(mfpMapped && meterUnmapped)
        return "meter replacement done.";
        else
            return "something went wrong.";

        }

      public Boolean updateMFPMapping(MeterFeederPlantMappingBean oldMFPMapping, MeterFeederPlantMappingBean newMFPMapping, String newMeterNo, String meterType, Date replaceDate)
        {

            switch(meterType)
            {
                case "MAIN":
                             newMFPMapping.setMainMeterNo(newMeterNo);
                             newMFPMapping.setCheckMeterNo(oldMFPMapping.getCheckMeterNo());
                    break;
                case "CHECK":
                    newMFPMapping.setCheckMeterNo(newMeterNo);
                    newMFPMapping.setMainMeterNo(oldMFPMapping.getMainMeterNo());
                    break;
                case "STANDBY":
                    newMFPMapping.setStandbyMeterNo(newMeterNo);
                    break;
                default:
                    break;
             }

            //Timestamp replacementDate = new Timestamp(replaceDate.getTime()) ;



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


            int updateRecord=0;
            System.out.println("old bean ="+oldMFPMapping+" \n new bean="+ newMFPMapping);
           // try {
               meterFeederPlantMappingService.updateMFPMapping(oldMFPMapping.getId(), replacementDate);
               newMFPMapping = meterFeederPlantMappingService.updateMFPMapping(newMFPMapping);
//                }catch (Exception e){
//                System.out.println("exception in updateMFPMapping ="+ e.getMessage());
//                return false;
//               }
            if(newMFPMapping!=null)
            return true;
            else
                return false;
       }
    private Boolean updateMeterStatusAndMappingByMeterNo(String oldMeterNumber,String status, String isMapped) {
      try {
          meterMasterService.updateMeterStatusAndMappingByMeterNo(oldMeterNumber, status, isMapped);
      }
      catch(Exception e)
      { e.printStackTrace();
        return false;
      }

      return true;
    }


}
