package in.co.mpwin.rebilling.controller.feedermaster;

import in.co.mpwin.rebilling.beans.feedermaster.FeederMasterBean;
import in.co.mpwin.rebilling.controller.thirdparty.ThirdPartyController;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.feedermaster.FeederMasterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/feeder")
@CrossOrigin(origins="*")
public class FeederMasterController {
    private static final Logger logger = LoggerFactory.getLogger(FeederMasterController.class);
    @Autowired
    FeederMasterService feederMasterService;

    @RequestMapping(method= RequestMethod.GET,value="")
    public ResponseEntity<?> getAllFeederMaster(){
        final String methodName = "getAllFeederMaster() : ";
        logger.info(methodName + "called with parameters empty");
        ResponseEntity feederResp = null;
        try {
            String status = "active";
            List<FeederMasterBean> feederList = feederMasterService.getAllFeederMasterBean(status);

            if(feederList.size()>0)
            {
                feederResp = new ResponseEntity<>(feederList, HttpStatus.OK);
            }
            else if(feederList.size()==0)
            {
                feederResp=new ResponseEntity<>(new Message("Feeder list is not available"),HttpStatus.BAD_REQUEST);
            }
            logger.info(methodName + "return with FeederMasterBean list of size : {} ",feederList.size());
        }catch(ApiException apiException) {
            feederResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch(DataIntegrityViolationException e) {
            feederResp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
        } catch(Exception e) {
            feederResp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
        }
        return feederResp;
    }

    @RequestMapping(method= RequestMethod.GET,value="/locationId/{locationId}")
    public ResponseEntity<?> getAllFeederByLocationId(@PathVariable("locationId") String locationId){
        final String methodName = "getAllFeederByLocationId() : ";
        logger.info(methodName + "called with parameters locationId={}",locationId);
        ResponseEntity feederResp = null;
        try {
            String status = "active";
            List<FeederMasterBean> feederList = feederMasterService.getAllFeederByLocationId(locationId,status);
            if(feederList.size()>0)
            {
                feederResp = new ResponseEntity<>(feederList, HttpStatus.OK);
            }
            else if(feederList.size()==0)
            {
                feederResp=new ResponseEntity<>(new Message("Feeder list is not available"),HttpStatus.BAD_REQUEST);
            }
            logger.info(methodName + "return with FeederMasterBean list of size : {} ",feederList.size());
        }catch(ApiException apiException) {
            feederResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        }catch(DataIntegrityViolationException e) {
            feederResp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
        }catch(Exception e) {
            feederResp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
        }
        return feederResp;
    }

    @RequestMapping(method = RequestMethod.POST,value = "")
    public ResponseEntity<?> createFeederMaster(@Valid @RequestBody FeederMasterBean feederMasterBean){
        final String methodName = "createFeederMaster() : ";
        logger.info(methodName + "called with parameter feederMasterBean={}",feederMasterBean);
        FeederMasterBean fmb = new FeederMasterBean();
        ResponseEntity feederInsrtResp = null;
        try {

            fmb = feederMasterService.createFeederMaster(feederMasterBean);
            //meterInsrtResp = new ResponseEntity<>(meterMasterBean.getMeterNumber()+" is created successfully", HttpStatus.OK);
            feederInsrtResp =  new ResponseEntity<>(new Message(fmb.getFeederNumber() + " is created successfully."),HttpStatus.OK);
            logger.info(methodName + "return with FeederMasterBean saved successfully : {} ",feederInsrtResp);
        }catch(ApiException apiException) {
            feederInsrtResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        }catch(DataIntegrityViolationException e) {
            feederInsrtResp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
        }catch(Exception e) {
            feederInsrtResp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
        }
        return feederInsrtResp;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/feederNumber/{feederNumber}")
    public ResponseEntity<?> getFeederByFeederNumber(@PathVariable("feederNumber") String feederNumber){
        final String methodName = "getFeederByFeederNumber() : ";
        logger.info(methodName + "called with parameter feederNumber={}",feederNumber);
        String status = "active";
        ResponseEntity feederResp = null;
        FeederMasterBean feeder = null;
        try {
            feeder = feederMasterService.getFeederByFeederNumber(feederNumber, status);
            if (feeder != null) {
                feederResp = new ResponseEntity<>(feeder, HttpStatus.OK);
            } else if (feeder == null) {
                feederResp = new ResponseEntity<>(new Message(feederNumber + " number does not exist."), HttpStatus.BAD_REQUEST);
            }
            logger.info(methodName + "return with FeederMasterBean : {} ",feeder);

        }catch(ApiException apiException) {
            feederResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        }catch(DataIntegrityViolationException e) {
            feederResp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
        }catch(Exception e) {
            feederResp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
        }
        return feederResp;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/id/{id}")
    public ResponseEntity<?> getFeederById(@PathVariable("id") Long id){
        final String methodName = "getFeederById() : ";
        logger.info(methodName + "called with parameter id={}",id);
        String status = "active";
        ResponseEntity feederResp = null;
        FeederMasterBean feeder = null;
        try {
            feeder = feederMasterService.getFeederById(id, status);
            if (feeder != null) {
                feederResp = new ResponseEntity<>(feeder, HttpStatus.OK);
            } else if (feeder == null) {
                feederResp = new ResponseEntity<>(new Message(id + " id does not exist."), HttpStatus.BAD_REQUEST);
            }
            logger.info(methodName + "return with FeederMasterBean : {} ",feeder);
        }catch(ApiException apiException) {
            feederResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        }catch(DataIntegrityViolationException e) {
            feederResp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
        }catch(Exception e) {
            feederResp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
        }
        return feederResp;
    }
    // get filtered feeder list for mfp mapping with the help of selected plant's location id
    @RequestMapping(method= RequestMethod.GET,value="/list/locationId/{locationId}")
    public ResponseEntity<?> getAllFeederByLocationIdForMapping(@PathVariable("locationId") String locationId){
        final String methodName = "getAllFeederByLocationIdForMapping() : ";
        logger.info(methodName + "called with parameter locationId={}",locationId);
        ResponseEntity feederResp = null;
        try {
            String status = "active";
            List<FeederMasterBean> feederList = feederMasterService.getAllFeederByLocationId(locationId,status);
            feederResp = new ResponseEntity<>(feederList, HttpStatus.OK);
            logger.info(methodName + "return with FeederMasterBean list of size : {} ",feederList.size());

        }catch(ApiException apiException) {
            feederResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        }catch(DataIntegrityViolationException e) {
            feederResp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
        }catch(Exception e) {
            feederResp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
        }
        return feederResp;
    }


}
