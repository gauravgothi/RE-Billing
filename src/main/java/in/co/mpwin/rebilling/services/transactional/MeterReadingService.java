package in.co.mpwin.rebilling.services.transactional;

import in.co.mpwin.rebilling.beans.readingoperations.MeterReading;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.repositories.transactional.MeterReadingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeterReadingService {
    @Autowired
    MeterReadingRepo meterReadingRepo;

    public List<MeterReading> getAllMeterReading(String status){
        List<MeterReading> meterReadingList = new ArrayList<>();
        try {
            meterReadingList = meterReadingRepo.findAllByStatus(status);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return meterReadingList;
    }

    public MeterReading createMeterReading(MeterReading meterReading){
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
    }
}
