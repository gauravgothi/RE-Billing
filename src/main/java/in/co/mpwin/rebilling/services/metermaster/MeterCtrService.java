package in.co.mpwin.rebilling.services.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterCtr;
import in.co.mpwin.rebilling.dao.metermaster.MeterCtrDao;
import in.co.mpwin.rebilling.repositories.metermaster.MeterCtrRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeterCtrService {

    @Autowired
    MeterCtrDao meterCtrDao;
    @Autowired
    private MeterCtrRepo meterCtrRepo;

    public List<MeterCtr> findAllByStatus(String status){
        List<MeterCtr> meterCtrList = new ArrayList<>();
        try {
            meterCtrList = meterCtrDao.findAllByStatus(status);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return meterCtrList;
    }

//    public MeterCtr findByCapacity(String capacity) {
//        MeterCtr meterCtr = new MeterCtr();
//        try {
//            meterCtr = meterCtrDao.findByCapacity(capacity);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        return meterCtr;
//    }
}
