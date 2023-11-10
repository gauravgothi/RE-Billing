package in.co.mpwin.rebilling.services.plantmaster;



import in.co.mpwin.rebilling.beans.plantmaster.PlantMasterBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.ValidatorService;
import in.co.mpwin.rebilling.repositories.mapping.MeterFeederPlantMappingRepo;
import in.co.mpwin.rebilling.repositories.plantmaster.PlantMasterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class PlantMasterService {

    @Autowired
    PlantMasterRepo plantMasterRepo;

    @Autowired private MeterFeederPlantMappingRepo meterFeederPlantMappingRepo;

    public List<PlantMasterBean> getAllPlantMasterBean(String status){
        List<PlantMasterBean> allPlantList = new ArrayList<>();
        try {
            allPlantList= plantMasterRepo.findAllByStatus(status);
            if (allPlantList.isEmpty())
                throw new ApiException(HttpStatus.BAD_REQUEST, "plant list are not found.");
            return allPlantList;
        } catch (ApiException apiException) {
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            throw d;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public PlantMasterBean createPlantMaster(PlantMasterBean plantMasterBean) {
         PlantMasterBean pmb = new PlantMasterBean();
        try {
            //check for existence of plant if already created with same plant name
            PlantMasterBean temp = plantMasterRepo.findByPlantNameIgnoreCaseAndStatus(plantMasterBean.getPlantName(),"active");
            if(temp!=null) {
                throw new ApiException(HttpStatus.BAD_REQUEST,"Plant with same name is already exist with plant code: "+temp.getPlantCode()+" and plant name: "+temp.getPlantName());
            }
            //Set the Audit control parameters, Globally
            new AuditControlServices().setInitialAuditControlParameters(plantMasterBean);
            //get max plant code and set new code = code+1
            plantMasterBean.setPlantCode("PC"+String.format("%03d",getMaxPlantCode()+1));
           plantMasterBean.setContactNo(new ValidatorService().removeSpaceFromString(plantMasterBean.getContactNo()));
           pmb = plantMasterRepo.save(plantMasterBean);
        }catch (ApiException apiException) {
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            throw d;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return pmb;
    }

    public PlantMasterBean getPlantByPlantCode(String plantCode, String status){
        PlantMasterBean plantMasterBean = new PlantMasterBean();
        try{
            plantMasterBean = plantMasterRepo.findByPlantCodeAndStatus(plantCode,status);
        }catch (ApiException apiException) {
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            throw d;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return plantMasterBean;
    }


    public PlantMasterBean getPlantById(Long id, String status){
        PlantMasterBean plantMasterBean = new PlantMasterBean();
        try{
            plantMasterBean = plantMasterRepo.findByIdAndStatus(id,status);
        }catch (ApiException apiException) {
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            throw d;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return plantMasterBean;
    }



    public Integer getMaxPlantCode()
    {
        Integer max = plantMasterRepo.findMaxInvestorCode();
        if(max==null)
            max=0;
        return max;
    }

    public List<PlantMasterBean> getAllPlantByLocationId(String locationId, String status) {
        List<PlantMasterBean> allPlantList = new ArrayList<>();
        try {
            allPlantList = plantMasterRepo.findByLocationIdAndStatus(locationId, status);
            if (allPlantList.isEmpty())
                throw new ApiException(HttpStatus.BAD_REQUEST, "plant list are not available for this location id:" + locationId);
            return allPlantList;
        } catch (ApiException apiException) {
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            throw d;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<PlantMasterBean> getAllPlantMasterBeanByDeveloperId(String developerId){
        List<PlantMasterBean> allPlantList;
        try {
            LocalDate endDate = LocalDate.now();
            List<String> mappedPlants = meterFeederPlantMappingRepo.findDistinctPlantCodeByDeveloperIdAndEndDateAndStatus(developerId,endDate,"active");
            if(mappedPlants.isEmpty())
                throw new ApiException(HttpStatus.BAD_REQUEST, "distinct plant code mapped with developer id "+developerId+" are not found in mfp mapping.");
            allPlantList = plantMasterRepo.findByPlantCodeListAndStatus(mappedPlants,"active");
            if (allPlantList.isEmpty())
                throw new ApiException(HttpStatus.BAD_REQUEST, "plant list mapped with developer id "+developerId+" are not found.");
            return allPlantList;
        } catch (ApiException apiException) {
            throw apiException;
        } catch (DataIntegrityViolationException d) {
            throw d;
        } catch (Exception e) {
            throw e;
        }
    }


}



