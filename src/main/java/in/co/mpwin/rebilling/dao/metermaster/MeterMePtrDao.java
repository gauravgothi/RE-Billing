package in.co.mpwin.rebilling.dao.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterMePtr;
import in.co.mpwin.rebilling.repositories.metermaster.MeterMePtrRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeterMePtrDao {

    @Autowired
    MeterMePtrRepo meterMePtrRepo;

    public List<MeterMePtr> getMeterMePtrDetails() {

        try {
            return meterMePtrRepo.findAll();
        } catch (Exception e) {
            System.out.print(e);
            e.printStackTrace();
        }

        return null;
    }
}
