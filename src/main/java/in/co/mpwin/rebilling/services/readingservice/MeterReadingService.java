package in.co.mpwin.rebilling.services.readingservice;

import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.repositories.readingrepo.MeterReadingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeterReadingService {
    @Autowired
    MeterReadingRepo meterReadingRepo;

    /*@Autowired
    MeterReadingBean meterReadingBean;*/

    public List<MeterReadingBean> getAllMeterReading(String status){
        List<MeterReadingBean> meterReadingBeanList = new ArrayList<>();
        try {
            meterReadingBeanList = meterReadingRepo.findAllByStatus(status);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return meterReadingBeanList;
    }

    /*public MeterReading createMeterReading(MeterReading meterReading){
        MeterReading meterReading1 = new MeterReading();
        try {
            //Set the Audit control parameters, Globally
            new AuditControlServices().setInitialAuditControlParameters(meterReading);
            meterReading1 = meterReadingRepo.save(meterReading);
        }catch (Exception exception){
            exception.printStackTrace();
            return null;
        }
        return meterReading1;
    }*/

    @Transactional
    public MeterReadingBean createMeterReading(MeterReadingBean passMRB) {
        MeterReadingBean meterReadingBean=new MeterReadingBean();
        try {
            //XmlParserBean parsedData = xmlSaxParserService.parseXml(xmlData);
//            System.out.println("Test data printed "+parsedData.getG1());
             meterReadingBean = meterReadingRepo.save(passMRB);
        }

        catch (DataIntegrityViolationException d)
        {
            throw d;
        }
        catch (Exception e) {
            throw e;

            // Handle the exception or log the error as needed
        }
        return meterReadingBean;
    }
}
