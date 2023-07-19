package in.co.mpwin.rebilling.dao.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterCtr;
import in.co.mpwin.rebilling.beans.metermaster.MeterMePtr;
import in.co.mpwin.rebilling.repositories.metermaster.MeterMePtrRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeterMePtrDao {

    @Autowired
    MeterMePtrRepo meterMePtrRepo;

    public List<MeterMePtr> findAllByStatus(String status) {
        List<MeterMePtr> meterMePtrList = new ArrayList<>();
        try {
            meterMePtrList =  meterMePtrRepo.findAllByStatus(status);
        } catch (Exception e) {
            System.out.print(e);
            e.printStackTrace();
        }

        return meterMePtrList;
    }
}
