package in.co.mpwin.rebilling.controller.plantmaster;


import in.co.mpwin.rebilling.beans.plantmaster.PlantMasterBean;
import in.co.mpwin.rebilling.controller.metermaster.MeterReplacementController;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.plantmaster.PlantMasterService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plant")
@CrossOrigin(origins="*")
public class PlantMasterController {
    private static final Logger logger = LoggerFactory.getLogger(PlantMasterController.class);
    @Autowired
    PlantMasterService plantMasterService;
    @RequestMapping(method= RequestMethod.GET,value="")
    public ResponseEntity<PlantMasterBean> getAllPlantMaster(){
        final String methodName = "getAllPlantMaster() : ";
        logger.info(methodName + "called with parameters empty");
        ResponseEntity plantResp = null;
        try {
            String status = "active";
            List<PlantMasterBean> plantList = plantMasterService.getAllPlantMasterBean(status);
            plantResp = new ResponseEntity<>(plantList, HttpStatus.OK);
            logger.info(methodName + "return with PlantMasterBean list of size : {} ",plantList.size());
            }catch(ApiException apiException) {
            plantResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch(DataIntegrityViolationException e) {
            plantResp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
            } catch(Exception e) {
            plantResp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
            }
            return plantResp;
    }


    @RequestMapping(method = RequestMethod.POST,value = "")
    public ResponseEntity<?> createPlantMaster(@Valid @RequestBody PlantMasterBean plantMasterBean){
        final String methodName = "createPlantMaster() : ";
        logger.info(methodName + "called with parameters plantMasterBean={}",plantMasterBean);
        PlantMasterBean pmb = new PlantMasterBean();
        ResponseEntity resp = null;
        try {

            pmb = plantMasterService.createPlantMaster(plantMasterBean);

            if(pmb!=null)
            {
                resp =  new ResponseEntity<>(new Message(pmb.getPlantCode() + " is created successfully."),HttpStatus.OK);
            }else if(pmb==null) {
                resp = new ResponseEntity<>(new Message(" plant could not created due to some error."), HttpStatus.BAD_REQUEST);
            }
            logger.info(methodName + "return with PlantMasterBean : {} ",pmb);
        }catch(ApiException apiException) {
            resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch(DataIntegrityViolationException e) {
            resp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
        } catch(Exception e) {
            resp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
        }
        return resp;
    }


    @RequestMapping(method = RequestMethod.GET,value = "/plantCode/{plantCode}")
    public ResponseEntity<?> getPlantByPlantCode(@PathVariable("plantCode") String plantCode){
        final String methodName = "getPlantByPlantCode() : ";
        logger.info(methodName + "called with parameters plantCode={}",plantCode);
        String status = "active";
        ResponseEntity plantResp = null;
        PlantMasterBean plantBean = null;
        try {
            plantBean = plantMasterService.getPlantByPlantCode(plantCode, status);
            if (plantBean!= null) {
                plantResp = new ResponseEntity<>(plantBean, HttpStatus.OK);
            } else if (plantBean == null) {
                plantResp = new ResponseEntity<>(new Message(plantCode + " number does not exist."), HttpStatus.BAD_REQUEST);
            } else {
                plantResp= new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }
            logger.info(methodName + "return with PlantMasterBean : {} ",plantBean);

            } catch(ApiException apiException) {
                plantResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch(DataIntegrityViolationException e) {
                plantResp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
            } catch(Exception e) {
                plantResp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
            }
        return plantResp;
    }


    @RequestMapping(method = RequestMethod.GET,value = "/id/{id}")
    public ResponseEntity<?> getPlantById(@PathVariable("id") Long id){
        final String methodName = "getPlantById() : ";
        logger.info(methodName + "called with parameters id={}",id);
        String status = "active";
        ResponseEntity plantResp = null;
        PlantMasterBean plantBean = null;
        try {
            plantBean = plantMasterService.getPlantById(id, status);
            if (plantBean  != null) {
                plantResp = new ResponseEntity<>(plantBean , HttpStatus.OK);
            } else if (plantBean  == null) {
                plantResp = new ResponseEntity<>(new Message(id + " id does not exist."), HttpStatus.BAD_REQUEST);
            } else {
                plantResp = new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }
            logger.info(methodName + "return with PlantMasterBean : {} ",plantBean);

            } catch(ApiException apiException) {
                plantResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch(DataIntegrityViolationException e) {
                plantResp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
            } catch(Exception e) {
                plantResp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
            }
        return plantResp;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/locationId/{locationId}")
    public ResponseEntity<?> getPlantByLocationId(@PathVariable("locationId") String locationId){
        final String methodName = "getPlantByLocationId() : ";
        logger.info(methodName + "called with parameters locationId={}",locationId);
        String status = "active";
        ResponseEntity plantResp = null;
         try {
            List<PlantMasterBean> plantList = plantMasterService.getAllPlantByLocationId(locationId, status);
            if (plantList.size()>0)
                plantResp = new ResponseEntity<>(plantList , HttpStatus.OK);
             else if (plantList.size() == 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "plant list are not available for this location id:"+locationId);
             else
                throw new ApiException(HttpStatus.BAD_REQUEST, "something went wrong to get plant list "+locationId);
             logger.info(methodName + "return with PlantMasterBean list of size : {} ",plantList.size());
             } catch(ApiException apiException) {
                 plantResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                 logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
             } catch(DataIntegrityViolationException e) {
                 plantResp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
                 logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
             } catch(Exception e) {
                 plantResp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
                 logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
             }
        return plantResp;
    }

    @RequestMapping(method= RequestMethod.GET,value="/list")
    public ResponseEntity<PlantMasterBean> getAllPlantMasterForMfpMapping(){
        final String methodName = "getAllPlantMasterForMfpMapping() : ";
        logger.info(methodName + "called with parameters empty");
        ResponseEntity plantResp = null;
        try {
            String status = "active";
            List<PlantMasterBean> plantList = plantMasterService.getAllPlantMasterBean(status);
            plantResp = new ResponseEntity<>(plantList, HttpStatus.OK);
            logger.info(methodName + "return with PlantMasterBean list of size : {} ",plantList.size());
            } catch(ApiException apiException) {
                plantResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch(DataIntegrityViolationException e) {
                plantResp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
            } catch(Exception e) {
                plantResp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
            }
        return plantResp;
    }

    // to get the plant list for investor machine mapping where plant should exit in mfp mapping with developer
    @RequestMapping(method= RequestMethod.GET,value="/list/developerId/{developerId}")
    public ResponseEntity<PlantMasterBean> getAllPlantMasterForInvestorMachineMapping(@PathVariable String developerId){
        final String methodName = "getAllPlantMasterForInvestorMachineMapping() : ";
        logger.info(methodName + "called with parameters developerId={}",developerId);
        ResponseEntity plantResp = null;
        try {
            List<PlantMasterBean> plantList = plantMasterService.getAllPlantMasterBeanByDeveloperId(developerId);
            plantResp = new ResponseEntity<>(plantList, HttpStatus.OK);
            logger.info(methodName + "return with PlantMasterBean list of size : {} ",plantList.size());
            } catch(ApiException apiException) {
                plantResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch(DataIntegrityViolationException e) {
                plantResp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
            } catch(Exception e) {
                plantResp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
            }
        return plantResp;
    }
// api for getting unmapped plant for mfp mapping
    @RequestMapping(method= RequestMethod.GET,value="/unmapped")
    public ResponseEntity<PlantMasterBean> getAllUnMappedPlants(){
        final String methodName = "getAllUnMappedPlants() : ";
        logger.info(methodName + "called with parameters empty");
        ResponseEntity plantResp = null;
        try {
            List<PlantMasterBean> plantList = plantMasterService.getAllUnMappedPlants();
            plantResp = new ResponseEntity<>(plantList, HttpStatus.OK);
            logger.info(methodName + "return with PlantMasterBean list of size : {} ",plantList.size());
        }catch(ApiException apiException) {
            plantResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch(DataIntegrityViolationException e) {
            plantResp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
        } catch(Exception e) {
            plantResp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
        }
        return plantResp;
    }



}



