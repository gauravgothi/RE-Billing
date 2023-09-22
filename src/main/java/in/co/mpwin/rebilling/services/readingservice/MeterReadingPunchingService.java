package in.co.mpwin.rebilling.services.readingservice;

import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.repositories.readingrepo.MeterReadingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MeterReadingPunchingService {


    @Autowired
    MeterReadingService meterReadingService;

    @Autowired
    MeterReadingRepo meterReadingRepo;


    public MeterReadingBean saveMeterReading(MeterReadingBean meterReadingBean)
    {

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

            return meterReadingService.createMeterReading(meterReadingBean);
            }catch(ApiException apiException)
            {
            throw apiException;
            }
            catch (DataIntegrityViolationException ex)
            {
            throw ex;
            }catch(NullPointerException ex)
            {
            throw ex;
            }catch(Exception ex)
            {
            throw ex;
            }
    }

    void validateMeterReading(MeterReadingBean meterReadingBean,MeterReadingBean previousReadingBean)
    {
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
        if(meterReadingBean.getIActiveEnergy().compareTo(previousReadingBean.getIActiveEnergy())<0)
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
            throw new ApiException(HttpStatus.BAD_REQUEST,"import Tod4 is less than previous reading");

    }
}
