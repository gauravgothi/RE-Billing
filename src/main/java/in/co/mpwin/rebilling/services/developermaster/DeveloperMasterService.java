package in.co.mpwin.rebilling.services.developermaster;

import in.co.mpwin.rebilling.beans.developermaster.DeveloperMasterBean;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.ValidatorService;
import in.co.mpwin.rebilling.repositories.developermaster.DeveloperMasterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeveloperMasterService {

    @Autowired
    DeveloperMasterRepo developerMasterRepo;

    public List<DeveloperMasterBean> getAllDeveloperMasterBean(String status)   {
        List<DeveloperMasterBean> developerMasterBeanList = new ArrayList<>();
        try {
            developerMasterBeanList = developerMasterRepo.findAllByStatus(status);
        }catch (Exception e){
            e.printStackTrace();
        }
        return developerMasterBeanList;
    }

    public List<DeveloperMasterBean> getAllDeveloperByLocationId(String locationId,String status)   {
        List<DeveloperMasterBean> developerMasterBeanList = new ArrayList<>();
        try {
            developerMasterBeanList = developerMasterRepo.findByLocationIdAndStatus(locationId,status);
        }catch (Exception e){
            e.printStackTrace();
        }
        return developerMasterBeanList;
    }

    public DeveloperMasterBean getDeveloperById(Long id, String status){
        DeveloperMasterBean developerMasterBean = new DeveloperMasterBean();
        try {
            developerMasterBean = developerMasterRepo.findByIdAndStatus(id, status);
        }catch (Exception e){
            e.printStackTrace();
        }
        return developerMasterBean;
    }

    public DeveloperMasterBean createDeveloperMaster(DeveloperMasterBean developerMasterBean){
        DeveloperMasterBean dmb = new DeveloperMasterBean();
        try {

            //Set the Audit control parameters, Globally
            new AuditControlServices().setInitialAuditControlParameters(developerMasterBean);
            //Validate the meterno remove the space.
            developerMasterBean.setCin(new ValidatorService().removeSpaceFromString(developerMasterBean.getCin()));
            developerMasterBean.setOfficeContactNo(new ValidatorService().removeSpaceFromString(developerMasterBean.getOfficeContactNo()));
            developerMasterBean.setSiteContactNo(new ValidatorService().removeSpaceFromString(developerMasterBean.getSiteContactNo()));

            dmb = developerMasterRepo.save(developerMasterBean);
        }catch (Exception exception){
            exception.printStackTrace();
            return null;
        }

        return dmb;
    }


}
