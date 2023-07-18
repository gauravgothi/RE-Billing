package in.co.mpwin.rebilling.dao.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterCtr;
import in.co.mpwin.rebilling.beans.metermaster.MeterMeCtr;
import in.co.mpwin.rebilling.repositories.metermaster.MeterCtrRepo;
import in.co.mpwin.rebilling.repositories.metermaster.MeterMeCtrRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeterMeCtrDao {
    @Autowired
    MeterMeCtrRepo meterMeCtrRepo;

    public List<MeterMeCtr> findAllByStatus(String status){
        List<MeterMeCtr> meterMeCtrList = new ArrayList<>();
        try {
            meterMeCtrList = meterMeCtrRepo.findAllByStatus(status);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return meterMeCtrList;
    }
}
