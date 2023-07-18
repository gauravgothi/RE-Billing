package in.co.mpwin.rebilling.services.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterCtr;
import in.co.mpwin.rebilling.beans.metermaster.MeterMake;
import in.co.mpwin.rebilling.dao.metermaster.MeterMakeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeterMakeService {

    @Autowired
    MeterMakeDao meterMakeDao;

    public List<MeterMake> findAllByStatus(String status){
        List<MeterMake> meterMakeList = new ArrayList<>();
        try {
            meterMakeList = meterMakeDao.findAllByStatus(status);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return meterMakeList;
    }
}
