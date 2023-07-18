package in.co.mpwin.rebilling.services.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterCtr;
import in.co.mpwin.rebilling.beans.metermaster.MeterMeCtr;
import in.co.mpwin.rebilling.dao.metermaster.MeterCtrDao;
import in.co.mpwin.rebilling.dao.metermaster.MeterMeCtrDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeterMeCtrService {

    @Autowired
    MeterMeCtrDao meterMeCtrDao;

    public List<MeterMeCtr> findAll(){
        List<MeterMeCtr> meterMeCtrList = new ArrayList<>();
        try {
            meterMeCtrList = meterMeCtrDao.findAll();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return meterMeCtrList;
    }
}
