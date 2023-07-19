package in.co.mpwin.rebilling.services.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterMePtr;
import in.co.mpwin.rebilling.beans.metermaster.MeterPtr;
import in.co.mpwin.rebilling.dao.metermaster.MeterMePtrDao;
import in.co.mpwin.rebilling.repositories.metermaster.MeterMePtrRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeterMePtrService {
    @Autowired
    MeterMePtrDao meterMePtrDao;
    public List<MeterMePtr> findAllByStatus(String status) {
        List<MeterMePtr> meterMePtrList = new ArrayList<>();
        try {
            meterMePtrList =  meterMePtrDao.findAllByStatus(status);

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return meterMePtrList;
    }


}
