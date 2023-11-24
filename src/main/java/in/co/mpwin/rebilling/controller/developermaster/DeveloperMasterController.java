package in.co.mpwin.rebilling.controller.developermaster;

import in.co.mpwin.rebilling.beans.developermaster.DeveloperMasterBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.developermaster.DeveloperMasterService;
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
@RequestMapping("/developer")
@CrossOrigin(origins="*")
public class DeveloperMasterController {

    private static final Logger logger = LoggerFactory.getLogger(DeveloperMasterController.class);
    @Autowired
    DeveloperMasterService developerMasterService;

    @RequestMapping(method= RequestMethod.GET,value="")
    public ResponseEntity<?> getAllDeveloperMaster(){
        final String methodName = "getAllDeveloperMaster() : ";
        logger.info(methodName + "called. with parameters empty: ");
        ResponseEntity developerResp = null;
        try {
            String status = "active";
            List<DeveloperMasterBean> developerMasterBeanList = developerMasterService.getAllDeveloperMasterBean(status);
            if (developerMasterBeanList.isEmpty())
               throw new ApiException(HttpStatus.BAD_REQUEST,"Developer list is not available.");
            developerResp = new ResponseEntity<>(developerMasterBeanList, HttpStatus.OK);
            logger.info(methodName + "return. developerMasterBeanList of size: {} ",developerMasterBeanList.size());
            }catch (ApiException apiException) {
                developerResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch (DataIntegrityViolationException d) {
                developerResp = new ResponseEntity<>(new Message("Data Integrity Violation:"+d.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
            } catch (Exception e) {
               developerResp = new ResponseEntity<>(new Message("Exception: " + e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
            }
        return developerResp;
    }

    @RequestMapping(method= RequestMethod.GET,value="/locationId/{locationId}")
    public ResponseEntity<?> getAllDeveloperByLocationId(@PathVariable("locationId") String locationId){
        final String methodName = "getAllDeveloperByLocationId() : ";
        logger.info(methodName + "called. with parameters locationId: {}",locationId);
        ResponseEntity developerResp = null;
        try {
            String status = "active";
            List<DeveloperMasterBean> developerMasterBeanList = developerMasterService.getAllDeveloperByLocationId(locationId,status);
            if (!developerMasterBeanList.isEmpty())
                developerResp = new ResponseEntity<>(developerMasterBeanList, HttpStatus.OK);
            else if (developerMasterBeanList.isEmpty())
                developerResp = new ResponseEntity<>(new Message("Developer list is not available."),HttpStatus.BAD_REQUEST);
            logger.info(methodName + "return. developerMasterBeanList of size: {} ",developerMasterBeanList.size());
        }catch (ApiException apiException) {
            developerResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch (DataIntegrityViolationException d) {
            developerResp = new ResponseEntity<>(new Message("Data Integrity Violation:"+d.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
        } catch (Exception e) {
            developerResp = new ResponseEntity<>(new Message("Exception: " + e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return developerResp;
    }

    @RequestMapping(method = RequestMethod.POST,value = "")
    public ResponseEntity<?> createDeveloperMaster(@Valid @RequestBody DeveloperMasterBean developerMasterBean){
        final String methodName = "createDeveloperMaster() : ";
        logger.info(methodName + "called. with parameters developerMasterBean: {}",developerMasterBean.toString());
        DeveloperMasterBean dmb = new DeveloperMasterBean();
        ResponseEntity developerInsrtResp = null;
        try {

            dmb = developerMasterService.createDeveloperMaster(developerMasterBean);

            if(dmb!=null)
            {
                //meterInsrtResp = new ResponseEntity<>(meterMasterBean.getMeterNumber()+" is created successfully", HttpStatus.OK);
                developerInsrtResp =  new ResponseEntity<>(new Message(dmb.getDeveloperName() + " is created successfully."),HttpStatus.OK);
            }else if(dmb==null) {
                developerInsrtResp = new ResponseEntity<>(new Message("something went wrong"), HttpStatus.BAD_REQUEST);
            }else {
                developerInsrtResp = new ResponseEntity<>(new Message("something went wrong"), HttpStatus.BAD_REQUEST);
            }
            logger.info(methodName + "return. developerMasterBean: {} ",developerMasterBean.toString());
        }catch (ApiException apiException) {
            developerInsrtResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch (DataIntegrityViolationException d) {
            developerInsrtResp = new ResponseEntity<>(new Message("Data Integrity Violation:"+d.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
        } catch (Exception e) {
            developerInsrtResp = new ResponseEntity<>(new Message("Exception: " + e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return developerInsrtResp;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/id/{id}")
    public ResponseEntity<?> getDeveloperById(@PathVariable("id") Long id) {
        final String methodName = "getDeveloperById() : ";
        logger.info(methodName + "called. with parameters developer id: {}",id);
        String status = "active";
        ResponseEntity developerResp = null;
        try {
            DeveloperMasterBean developer = developerMasterService.getDeveloperById(id, status);
            if (developer != null) {
                developerResp = new ResponseEntity<>(developer, HttpStatus.OK);
            } else if (developer == null) {
                developerResp = new ResponseEntity<>(new Message(id + " id does not exist."), HttpStatus.BAD_REQUEST);
            } else {
                developerResp = new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }
            logger.info(methodName + "return. developerMasterBean: {} ",developer.toString());
        } catch (ApiException apiException) {
            developerResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch (DataIntegrityViolationException d) {
            developerResp = new ResponseEntity<>(new Message("Data Integrity Violation:"+d.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
        } catch (Exception e) {
            developerResp = new ResponseEntity<>(new Message("Exception: " + e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return developerResp;
    }

//    @GetMapping("/bifurcate")
//    public ResponseEntity<?> getBifurcateDto(MeterConsumptionDto dto){
//        ResponseEntity bifurcateResp = null;
//        try {
//            DeveloperMasterBean developer = developerMasterService.getBifurcateDto(dto);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return bifurcateResp;
//    }


}
