package in.co.mpwin.rebilling.services.plantmaster;



import in.co.mpwin.rebilling.beans.plantmaster.PlantMasterBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.AuditControlServices;
import in.co.mpwin.rebilling.miscellanious.ValidatorService;
import in.co.mpwin.rebilling.repositories.mapping.MeterFeederPlantMappingRepo;
import in.co.mpwin.rebilling.repositories.plantmaster.PlantMasterRepo;
import in.co.mpwin.rebilling.services.metermaster.MeterReplacementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class PlantMasterService {

    private static final Logger logger = LoggerFactory.getLogger(PlantMasterService.class);

    @Autowired
    PlantMasterRepo plantMasterRepo;

    @Autowired private MeterFeederPlantMappingRepo meterFeederPlantMappingRepo;

    public List<PlantMasterBean> getAllPlantMasterBean(String status){
        final String methodName = "getAllPlantMasterBean() : ";
        logger.info(methodName + "called with parameters status={}",status);

        List<PlantMasterBean> allPlantList = new ArrayList<>();
        try {
            allPlantList= plantMasterRepo.findAllByStatus(status);
            if (allPlantList.isEmpty())
                throw new ApiException(HttpStatus.BAD_REQUEST, "plant list are not found.");
            logger.info(methodName + " return with  PlantMasterBean list of size : {}", allPlantList.size());
            return allPlantList;
            } catch (ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
            }catch (DataIntegrityViolationException d){
                logger.error(methodName+" throw DataIntegrityViolationException");
                throw d;
            }catch (NullPointerException ex){
                logger.error(methodName+" throw NullPointerException");
                throw ex;
            } catch (Exception e) {
                logger.error(methodName+" throw Exception");
                throw e;
            }
    }


    public PlantMasterBean createPlantMaster(PlantMasterBean plantMasterBean) {
        final String methodName = "createPlantMaster() : ";
        logger.info(methodName + "called with parameters plantMasterBean={}", plantMasterBean);
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
        }catch (ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        }catch (NullPointerException ex){
            logger.error(methodName+" throw NullPointerException");
            throw ex;
        } catch (Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }
        logger.info(methodName + " return with  PlantMasterBean : {}",pmb);
        return pmb;
    }

    public PlantMasterBean getPlantByPlantCode(String plantCode, String status){
        final String methodName = "getPlantByPlantCode() : ";
        logger.info(methodName + "called with parameters plantCode={}, status={}",plantCode,status);
        PlantMasterBean plantMasterBean = new PlantMasterBean();
        try{
            plantMasterBean = plantMasterRepo.findByPlantCodeAndStatus(plantCode,status);
            }catch (ApiException apiException){
                logger.error(methodName+" throw apiException");
                throw apiException;
            }catch (DataIntegrityViolationException d){
                logger.error(methodName+" throw DataIntegrityViolationException");
                throw d;
            }catch (NullPointerException ex){
                logger.error(methodName+" throw NullPointerException");
                throw ex;
            } catch (Exception e) {
                logger.error(methodName+" throw Exception");
                throw e;
            }
        logger.info(methodName + " return with  PlantMasterBean : {}",plantMasterBean);
        return plantMasterBean;
    }


    public PlantMasterBean getPlantById(Long id, String status){
        final String methodName = "getPlantById() : ";
        logger.info(methodName + "called with parameters id={}, status={}",id,status);
        PlantMasterBean plantMasterBean = new PlantMasterBean();
        try{
            plantMasterBean = plantMasterRepo.findByIdAndStatus(id,status);
        }catch (ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        }catch (NullPointerException ex){
            logger.error(methodName+" throw NullPointerException");
            throw ex;
        } catch (Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }
        logger.info(methodName + " return with  PlantMasterBean : {}",plantMasterBean);
        return plantMasterBean;
    }



    public Integer getMaxPlantCode()
    {
        final String methodName = "getPlantById() : ";
        logger.info(methodName + "called with parameters empty");
        Integer max = plantMasterRepo.findMaxInvestorCode();
        if(max==null)
            max=0;
        logger.info(methodName + " return with  max : {}",max);
        return max;
    }

    public List<PlantMasterBean> getAllPlantByLocationId(String locationId, String status) {
        final String methodName = " getAllPlantByLocationId() : ";
        logger.info(methodName + "called with parameters locationId={}, status={}",locationId,status);
        List<PlantMasterBean> allPlantList = new ArrayList<>();
        try {
            allPlantList = plantMasterRepo.findByLocationIdAndStatus(locationId, status);
            if (allPlantList.isEmpty())
                throw new ApiException(HttpStatus.BAD_REQUEST, "plant list are not available for this location id:" + locationId);
            logger.info(methodName + " return with  PlantMasterBean list of size : {}",allPlantList.size());
            return allPlantList;
        } catch (ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        }catch (NullPointerException ex){
            logger.error(methodName+" throw NullPointerException");
            throw ex;
        } catch (Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }
    }

    public List<PlantMasterBean> getAllPlantMasterBeanByDeveloperId(String developerId){
        final String methodName = " getAllPlantMasterBeanByDeveloperId() : ";
        logger.info(methodName + "called with parameters locationId={}",developerId);
        List<PlantMasterBean> allPlantList;
        try {
            LocalDate endDate = LocalDate.now();
            List<String> mappedPlants = meterFeederPlantMappingRepo.findDistinctPlantCodeByDeveloperIdAndEndDateAndStatus(developerId,endDate,"active");
            if(mappedPlants.isEmpty())
                throw new ApiException(HttpStatus.BAD_REQUEST, "distinct plant code mapped with developer id "+developerId+" are not found in mfp mapping.");
            allPlantList = plantMasterRepo.findByPlantCodeListAndStatus(mappedPlants,"active");
            if (allPlantList.isEmpty())
                throw new ApiException(HttpStatus.BAD_REQUEST, "plant list mapped with developer id "+developerId+" are not found.");
            logger.info(methodName + " return with  PlantMasterBean list of size : {}",allPlantList.size());
            return allPlantList;
        } catch (ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        }catch (NullPointerException ex){
            logger.error(methodName+" throw NullPointerException");
            throw ex;
        } catch (Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }
    }

    public List<PlantMasterBean> getAllUnMappedPlants() {
        final String methodName = "getAllUnMappedPlants() : ";
        logger.info(methodName + "called with parameters empty");

        List<PlantMasterBean> unMappedPlantList ;
        try {
            LocalDate endDate = LocalDate.now();
            List<String> plantCodeInMfpMapping = meterFeederPlantMappingRepo.findDistinctPlantCodeByEndDateAndStatus(endDate,"active");
            // get unmapped plants excluding plant codes present in mfp mapping table.
            unMappedPlantList= plantMasterRepo.findUnmappedPlants(plantCodeInMfpMapping,"active");
            if (unMappedPlantList.isEmpty())
                throw new ApiException(HttpStatus.BAD_REQUEST, "plant list are not found.");
            logger.info(methodName + " return with  PlantMasterBean list of size : {}", unMappedPlantList.size());
            return unMappedPlantList;
        } catch (ApiException apiException){
            logger.error(methodName+" throw apiException");
            throw apiException;
        }catch (DataIntegrityViolationException d){
            logger.error(methodName+" throw DataIntegrityViolationException");
            throw d;
        }catch (NullPointerException ex){
            logger.error(methodName+" throw NullPointerException");
            throw ex;
        } catch (Exception e) {
            logger.error(methodName+" throw Exception");
            throw e;
        }

    }
}



