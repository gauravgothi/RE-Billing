package in.co.mpwin.rebilling.services.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterCtr;
import in.co.mpwin.rebilling.beans.metermaster.MeterDmf;
import in.co.mpwin.rebilling.dao.metermaster.MeterDmfDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeterDmfService {

    @Autowired
    MeterDmfDao meterDmfDao;

    public List<MeterDmf> findAllByStatus(String status){
        List<MeterDmf> meterDmfList = new ArrayList<>();
        try {
            meterDmfList = meterDmfDao.findAllByStatus(status);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return meterDmfList;
    }
}
