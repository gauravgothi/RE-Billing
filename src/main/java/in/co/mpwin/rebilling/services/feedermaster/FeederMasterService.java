package in.co.mpwin.rebilling.services.feedermaster;

import in.co.mpwin.rebilling.beans.feedermaster.FeederMasterBean;
import in.co.mpwin.rebilling.beans.metermaster.MeterMasterBean;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.ValidatorService;
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

    public FeederMasterBean createFeederMaster(FeederMasterBean feederMasterBean) {
        //int result = -1;
        FeederMasterBean fmb = new FeederMasterBean();
        try {
            //check for existence of feeder if already created with same feeder number
            FeederMasterBean temp = feederMasterRepo.findByFeederNumberAndStatus(feederMasterBean.getFeederNumber(),"active");
            if(temp!=null) {
                return null;
            }
            //Set the Audit control parameters, Globally
            new AuditControlServices().setInitialAuditControlParameters(feederMasterBean);
            //Validate the meterno remove the space.
            feederMasterBean.setFeederNumber(new ValidatorService().removeSpaceFromString(feederMasterBean.getFeederNumber()));
            fmb = feederMasterRepo.save(feederMasterBean);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return fmb;
    }

    public FeederMasterBean getFeederByFeederNumber(String feederNumber, String status){
        FeederMasterBean feederMasterBean = new FeederMasterBean();
        try{
            feederMasterBean = feederMasterRepo.findByFeederNumberAndStatus(feederNumber,status);
        }catch (Exception e){
            e.printStackTrace();
        }
        return feederMasterBean;
    }

    public FeederMasterBean getFeederById(Long id, String status){
        FeederMasterBean feederMasterBean = new FeederMasterBean();
        try{
            feederMasterBean = feederMasterRepo.findByIdAndStatus(id,status);
        }catch (Exception e){
            e.printStackTrace();
        }
        return feederMasterBean;
    }
}
