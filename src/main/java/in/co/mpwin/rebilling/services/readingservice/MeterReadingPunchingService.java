package in.co.mpwin.rebilling.services.readingservice;

import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.repositories.readingrepo.MeterReadingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class MeterReadingPunchingService {


    @Autowired
    MeterReadingService meterReadingService;

    @Autowired
    MeterReadingRepo meterReadingRepo;


    public MeterReadingBean saveMeterReading(MeterReadingBean meterReadingBean)
    {

        try {
            MeterReadingBean previousReading = meterReadingRepo.findJustBefore(meterReadingBean.getMeterNo(),meterReadingBean.getReadingDate());
            //if reading is not SR then take as normal
            meterReadingBean.setEndDate(meterReadingBean.getReadingDate());
            meterReadingBean.setCurrentState("initial_read");
            meterReadingBean.setReadingType("NR");
            meterReadingBean.setReadSource("web");
            if(previousReading==null)
                meterReadingBean.setReadingType("SR");

            return meterReadingService.createMeterReading(meterReadingBean);
            }catch(ApiException apiException)
            {
            throw apiException;
            }
            catch (DataIntegrityViolationException ex)
            {
            throw ex;
            }catch(Exception ex)
            {
            throw ex;
            }
    }
}
