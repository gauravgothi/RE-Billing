package in.co.mpwin.rebilling.dao.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterCtr;
import in.co.mpwin.rebilling.beans.metermaster.MeterMake;
import in.co.mpwin.rebilling.repositories.metermaster.MeterCtrRepo;
import in.co.mpwin.rebilling.repositories.metermaster.MeterMakeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeterMakeDao {
    @Autowired
    MeterMakeRepo meterMakeRepo;

    public List<MeterMake> findAllByStatus(String status){
        List<MeterMake> meterMakeList = new ArrayList<>();
        try {
            meterMakeList = meterMakeRepo.findAllByStatus(status);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return meterMakeList;
    }
}
