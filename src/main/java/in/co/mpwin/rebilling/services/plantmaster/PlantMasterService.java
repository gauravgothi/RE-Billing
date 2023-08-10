package in.co.mpwin.rebilling.services.plantmaster;



import in.co.mpwin.rebilling.beans.plantmaster.PlantMasterBean;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.ValidatorService;
import in.co.mpwin.rebilling.repositories.plantmaster.PlantMasterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;


@Service
public class PlantMasterService {

    @Autowired
    PlantMasterRepo plantMasterRepo;

    public List<PlantMasterBean> getAllPlantMasterBean(String status){
        List<PlantMasterBean> allPlantList = new ArrayList<>();
        try {
            allPlantList= plantMasterRepo.findAllByStatus(status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allPlantList;
    }


    public PlantMasterBean createPlantMaster(PlantMasterBean plantMasterBean) {
         PlantMasterBean pmb = new PlantMasterBean();
        try {
            //check for existence of plant if already created with same plant code
            PlantMasterBean temp = plantMasterRepo.findByPlantCodeAndStatus(plantMasterBean.getPlantCode(),"active");
            if(temp!=null) {
                return null;
            }
            //Set the Audit control parameters, Globally
            new AuditControlServices().setInitialAuditControlParameters(plantMasterBean);

            //get max sequence id and set  id as id+1
            plantMasterBean.setId(getMaxSequence()+1);
            // set plant code using id
           plantMasterBean.setPlantCode("P" + (getMaxSequence()+1));

           plantMasterBean.setContactNo(new ValidatorService().removeSpaceFromString(plantMasterBean.getContactNo()));

            pmb = plantMasterRepo.save(plantMasterBean);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return pmb;
    }

    public PlantMasterBean getPlantByPlantCode(String plantCode, String status){
        PlantMasterBean plantMasterBean = new PlantMasterBean();
        try{
            plantMasterBean = plantMasterRepo.findByPlantCodeAndStatus(plantCode,status);
        }catch (Exception e){
            e.printStackTrace();
        }
        return plantMasterBean;
    }


    public PlantMasterBean getPlantById(Long id, String status){
        PlantMasterBean plantMasterBean = new PlantMasterBean();
        try{
            plantMasterBean = plantMasterRepo.findByIdAndStatus(id,status);
        }catch (Exception e){
            e.printStackTrace();
        }
        return plantMasterBean;
    }



    public Long getMaxSequence()
    {
         return plantMasterRepo.getMaxSequence();

    }

    public List<PlantMasterBean> getAllPlantByLocationId(String locationId, String status) {
        List<PlantMasterBean> allPlantList = new ArrayList<>();
        try {
            allPlantList= plantMasterRepo.findByLocationIdAndStatus(locationId, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allPlantList;
    }
}



