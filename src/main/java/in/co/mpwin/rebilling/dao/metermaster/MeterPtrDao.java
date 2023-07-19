package in.co.mpwin.rebilling.dao.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterMePtr;
import in.co.mpwin.rebilling.beans.metermaster.MeterPtr;
import in.co.mpwin.rebilling.repositories.metermaster.MeterPtrRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeterPtrDao {
    @Autowired
    private MeterPtrRepo meterPtrRepo;

    public List<MeterPtr> findAllByStatus(String status) {
        List<MeterPtr> meterPtrList = new ArrayList<>();
        try {
            meterPtrList =  meterPtrRepo.findAllByStatus(status);
        } catch (Exception e) {
            System.out.print(e);
            e.printStackTrace();
        }

        return meterPtrList;
    }

    public MeterPtr getMeterPtrDetails(Long id) {

        try {
            return meterPtrRepo.findById(id).get();
        } catch (Exception e) {
            System.out.print(e);
            e.printStackTrace();
        }

        return null;
    }

}
