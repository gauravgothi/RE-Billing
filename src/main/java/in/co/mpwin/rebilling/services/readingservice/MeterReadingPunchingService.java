package in.co.mpwin.rebilling.services.readingservice;

import in.co.mpwin.rebilling.beans.metermaster.MeterMasterBean;
import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.repositories.readingrepo.MeterReadingRepo;
import in.co.mpwin.rebilling.services.metermaster.MeterMasterService;
import in.co.mpwin.rebilling.services.thirdparty.ThirdPartyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class MeterReadingPunchingService {

    private static final Logger logger = LoggerFactory.getLogger(MeterReadingPunchingService.class);
    @Autowired
    MeterReadingService meterReadingService;

    @Autowired
    MeterReadingRepo meterReadingRepo;
    @Autowired
    MeterMasterService meterMasterService;


    public MeterReadingBean saveMeterReading(MeterReadingBean meterReadingBean)
    {
        final String methodName = "saveMeterReading() : ";
        logger.info(methodName + "called with parameters meterReadingBean={}",meterReadingBean);

        try {
            MeterReadingBean previousReadingBean = meterReadingRepo.findJustBefore(meterReadingBean.getMeterNo(),meterReadingBean.getReadingDate());
            //meter reading validation before reading saving
            validateMeterReading(meterReadingBean,previousReadingBean);
            //if reading is not SR then take as normal
            meterReadingBean.setEndDate(new DateMethods().getOneDayBefore(meterReadingBean.getReadingDate()));
            meterReadingBean.setCurrentState("initial_read");
            meterReadingBean.setReadingType("NR");
            meterReadingBean.setReadSource("web");
            //if previous reading is not exist for meter than take SR reading(specially new plant installation)
            if(previousReadingBean==null)
                meterReadingBean.setReadingType("SR");
            MeterReadingBean savedReadBean = meterReadingService.createMeterReading(meterReadingBean);
            logger.info(methodName + " return with saved MeterReadingBean : {}",savedReadBean);
            return savedReadBean;
            }catch(NullPointerException ex)
            {
            logger.error(methodName+" throw NullPointerException");
            throw ex;
            }catch (ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
            }catch (DataIntegrityViolationException d){
                logger.error(methodName+" throw DataIntegrityViolationException");
                throw d;
            } catch (Exception e) {
                logger.error(methodName+" throw Exception");
                throw e;
            }

    }

    void validateMeterReading(MeterReadingBean meterReadingBean,MeterReadingBean previousReadingBean)
    {
        final String methodName = " validateMeterReading : ";
        logger.info(methodName + "called with parameters meterReadingBean={}, previousReadingBean={}",meterReadingBean,previousReadingBean);
       if(meterReadingBean==null)
           throw new ApiException(HttpStatus.BAD_REQUEST,"meter reading bean is null");
       //validation on assessment, adjustment and MD value to avoid negative value

       if(meterReadingBean.getEAssesment().compareTo(BigDecimal.valueOf(0))<0 || meterReadingBean.getEAdjustment().compareTo(BigDecimal.valueOf(0))<0)
         throw new ApiException(HttpStatus.BAD_REQUEST,"export assessment / adjustment value is negative");
        if(meterReadingBean.getIAssesment().compareTo(BigDecimal.valueOf(0))<0 || meterReadingBean.getIAdjustment().compareTo(BigDecimal.valueOf(0))<0)
            throw new ApiException(HttpStatus.BAD_REQUEST,"import assessment / adjustment value is negative");

        if(meterReadingBean.getEMaxDemand().compareTo(BigDecimal.valueOf(0))<0 || meterReadingBean.getIMaxDemand().compareTo(BigDecimal.valueOf(0))<0)
            throw new ApiException(HttpStatus.BAD_REQUEST,"export MD / import MD is negative");

        // kwh, kveh and tod reading can not less than previous reading
        if(meterReadingBean.getEActiveEnergy().compareTo(previousReadingBean.getEActiveEnergy())<0)
            throw new ApiException(HttpStatus.BAD_REQUEST,"export active energy is less than previous reading");
/*        if(meterReadingBean.getIActiveEnergy().compareTo(previousReadingBean.getIActiveEnergy())<0)
            throw new ApiException(HttpStatus.BAD_REQUEST,"import active energy is less than previous reading");
        if(meterReadingBean.getEKvah().compareTo(previousReadingBean.getEKvah())<0)

            throw new ApiException(HttpStatus.BAD_REQUEST,"export Kvah is less than previous reading");
        if(meterReadingBean.getIKvah().compareTo(previousReadingBean.getIKvah())<0)
            throw new ApiException(HttpStatus.BAD_REQUEST,"import Kvah is less than previous reading");

        if(meterReadingBean.getETod1().compareTo(previousReadingBean.getETod1())<0)
            throw new ApiException(HttpStatus.BAD_REQUEST,"export Tod1 is less than previous reading");
        if(meterReadingBean.getETod2().compareTo(previousReadingBean.getETod2())<0)
            throw new ApiException(HttpStatus.BAD_REQUEST,"export Tod2 is less than previous reading");
        if(meterReadingBean.getETod3().compareTo(previousReadingBean.getETod3())<0)
            throw new ApiException(HttpStatus.BAD_REQUEST,"export Tod3 is less than previous reading");
        if(meterReadingBean.getETod4().compareTo(previousReadingBean.getETod4())<0)
            throw new ApiException(HttpStatus.BAD_REQUEST,"export Tod4 is less than previous reading");

        if(meterReadingBean.getITod1().compareTo(previousReadingBean.getITod1())<0)
            throw new ApiException(HttpStatus.BAD_REQUEST,"import Tod1 is less than previous reading");
        if(meterReadingBean.getITod2().compareTo(previousReadingBean.getITod2())<0)
            throw new ApiException(HttpStatus.BAD_REQUEST,"import Tod2 is less than previous reading");
        if(meterReadingBean.getITod3().compareTo(previousReadingBean.getITod3())<0)
            throw new ApiException(HttpStatus.BAD_REQUEST,"import Tod3 is less than previous reading");
        if(meterReadingBean.getITod4().compareTo(previousReadingBean.getITod4())<0)
            throw new ApiException(HttpStatus.BAD_REQUEST,"import Tod4 is less than previous reading");*/
        logger.info(methodName + " return with void.");
    }

    public MeterReadingBean saveSRMeterReading(MeterReadingBean meterReadingBean) {
        final String methodName = " saveSRMeterReading : ";
        logger.info(methodName + "called with parameters meterReadingBean={}",meterReadingBean);
        try {
            MeterMasterBean mmb = null;
            mmb = meterMasterService.getMeterDetailsByMeterNo(meterReadingBean.getMeterNo(), "active");
            //check meter exist in meter master before crating sr reading
            if (mmb == null)
                throw new ApiException(HttpStatus.BAD_REQUEST, "Meter No. " + meterReadingBean.getMeterNo() + " detail is not available in meter master data.");
            //meter sr reading with meter create date
            Date meterCreateDate = new SimpleDateFormat("yyyy-MM-dd").parse(mmb.getInstallDate());
            if (meterReadingBean.getReadingDate().compareTo(meterCreateDate) < 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "Meter SR date can not less than meter create date.");
            meterReadingBean.setReadingType("SR");
            meterReadingBean.setReadSource("web");
            meterReadingBean.setEndDate(new DateMethods().getOneDayBefore(meterReadingBean.getReadingDate()));
            meterReadingBean.setCurrentState("ht_accept");
            meterReadingBean.setMf(BigDecimal.valueOf(0));
            MeterReadingBean lastReading = meterReadingRepo.findLastReadByMeterNoAndStatus(meterReadingBean.getMeterNo(),"active");

            if(lastReading!=null && (lastReading.getReadingType().compareTo("FR")==0)&&(meterReadingBean.getReadingDate().compareTo(lastReading.getReadingDate())>=0)) {
                logger.info(methodName + " return.");
                return meterReadingService.createMeterReading(meterReadingBean);
            }
            else if(lastReading==null) {
                logger.info(methodName + " return.");
                return meterReadingService.createMeterReading(meterReadingBean);
            }
            else
            throw new ApiException(HttpStatus.BAD_REQUEST, "Last reading of this meter is not FR or last reading date is greater than SR reading date.");
             } catch (ParseException e) {
              logger.error(methodName+" throw ParseException ");
              throw new RuntimeException(e);
            }catch (ApiException apiException){
                logger.error(methodName+" throw apiException");
                throw apiException;
            }catch (DataIntegrityViolationException d){
                logger.error(methodName+" throw DataIntegrityViolationException");
                throw d;
            } catch (Exception e) {
                logger.error(methodName+" throw Exception");
                throw e;
            }
    }
}
