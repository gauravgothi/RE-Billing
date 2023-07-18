package in.co.mpwin.rebilling.dao.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterCtr;
import in.co.mpwin.rebilling.beans.metermaster.MeterDmf;
import in.co.mpwin.rebilling.repositories.metermaster.MeterCtrRepo;
import in.co.mpwin.rebilling.repositories.metermaster.MeterDmfRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeterDmfDao {
    @Autowired
    MeterDmfRepo meterDmfRepo;

    public List<MeterDmf> findAll(){
        List<MeterDmf> meterDmfList = new ArrayList<>();
        try {
            meterDmfList = meterDmfRepo.findAll();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return meterDmfList;
    }
}
