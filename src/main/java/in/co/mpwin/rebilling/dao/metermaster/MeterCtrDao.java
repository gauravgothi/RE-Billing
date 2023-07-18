package in.co.mpwin.rebilling.dao.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterCtr;
import in.co.mpwin.rebilling.repositories.metermaster.MeterCtrRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeterCtrDao {
    @Autowired
    MeterCtrRepo meterCtrRepo;

    public List<MeterCtr> findAllByStatus(String status){
        List<MeterCtr> meterCtrList = new ArrayList<>();
        try {
            meterCtrList = meterCtrRepo.findAllByStatus(status);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return meterCtrList;
    }

//    public MeterCtr findByCapacity(String capacity) {
//        MeterCtr meterCtr = new MeterCtr();
//        try {
//            meterCtr = meterCtrRepo.findByCapacity(capacity);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        return meterCtr;
//    }
}
