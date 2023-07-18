package in.co.mpwin.rebilling.services.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterPtr;
import in.co.mpwin.rebilling.dao.metermaster.MeterPtrDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeterPtrService {
    @Autowired
    MeterPtrDao meterPtrDao;

    public List<MeterPtr> getMeterPtrDetails() {
        try {
            return meterPtrDao.getMeterPtrDetails();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return null;
    }

    public MeterPtr getMeterPtrDetails(Long id)
    {
        try {
            return meterPtrDao.getMeterPtrDetails(id);

        } catch(Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
        return null;
    }
}
