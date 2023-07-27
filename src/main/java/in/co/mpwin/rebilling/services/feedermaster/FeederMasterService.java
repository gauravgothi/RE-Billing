package in.co.mpwin.rebilling.services.feedermaster;

import in.co.mpwin.rebilling.beans.feedermaster.FeederMasterBean;
import in.co.mpwin.rebilling.repositories.feedermaster.FeederMasterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeederMasterService {

    @Autowired
    FeederMasterRepo feederMasterRepo;

    public List<FeederMasterBean> getAllFeederMasterBean(String status){
        List<FeederMasterBean> allFeederList = new ArrayList<>();
        try {
            allFeederList= feederMasterRepo.findAllByStatus(status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allFeederList;
    }
}
