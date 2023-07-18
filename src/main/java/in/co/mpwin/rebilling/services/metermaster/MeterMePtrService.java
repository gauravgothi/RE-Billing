package in.co.mpwin.rebilling.services.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterMePtr;
import in.co.mpwin.rebilling.beans.metermaster.MeterPtr;
import in.co.mpwin.rebilling.dao.metermaster.MeterMePtrDao;
import in.co.mpwin.rebilling.repositories.metermaster.MeterMePtrRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeterMePtrService {
    @Autowired
    MeterMePtrDao meterMePtrDao;
    public List<MeterMePtr> getMeterMePtrDetails(String status) {
        try {
            return meterMePtrDao.getMeterMePtrDetails(status);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return null;
    }


}
