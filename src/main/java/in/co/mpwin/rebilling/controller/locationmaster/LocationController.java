package in.co.mpwin.rebilling.controller.locationmaster;

import in.co.mpwin.rebilling.beans.locationmaster.LocationMaster;
import in.co.mpwin.rebilling.controller.feedermaster.FeederMasterController;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.repositories.locationmaster.LocationMasterRepo;
import in.co.mpwin.rebilling.services.locationmaster.LocationMasterService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/location")
@CrossOrigin(origins="*")
public class LocationController {

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);
    @Autowired
    LocationMasterService locationMasterService;

    @Autowired
    private ModelMapper mapper;
    @Autowired
    private LocationMasterRepo locationMasterRepo;

    @RequestMapping(method = RequestMethod.GET, value="")
    public ResponseEntity<LocationMaster> getAllLocationMaster() {
        final String methodName = "getAllLocationMaster() : ";
        logger.info(methodName + "called with parameters empty");
        String status = "active";
        ResponseEntity locationResp = null;
        try {
            List<LocationMaster> locationList = locationMasterService.findAllByStatus(status);
            if (!locationList.isEmpty()) {
                locationResp = new ResponseEntity<>(locationList, HttpStatus.OK);
            } else if (locationList.isEmpty()) {
                locationResp = new ResponseEntity<>(new Message("Locations list does not exist."), HttpStatus.BAD_REQUEST);
            }
            logger.info(methodName + "return with LocationMaster list of size : {} ",locationList.size());
            }catch(ApiException apiException) {
                locationResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch(DataIntegrityViolationException e) {
                locationResp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
            } catch(Exception e) {
                locationResp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
            }
        return locationResp;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/division/{division}")
    public ResponseEntity<LocationMaster> getLocationByDivisionCode(@PathVariable("division") String divisionCode) {
        final String methodName = "getLocationByDivisionCode() : ";
        logger.info(methodName + "called with parameter divisionCode ={}",divisionCode);
        String status = "active";
        ResponseEntity locationResp = null;
        LocationMaster location = null;
        try {
            location = locationMasterService.getLocationByDivisionCode(divisionCode, status);
            if (location != null) {
                locationResp = new ResponseEntity<>(location, HttpStatus.OK);
            } else if (location == null) {
                locationResp = new ResponseEntity<>(new Message("division code does not exist."), HttpStatus.BAD_REQUEST);
            }
            logger.info(methodName + "return with LocationMaster : {} ",location);

            } catch(ApiException apiException) {
                locationResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch(DataIntegrityViolationException e) {
                locationResp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
            } catch(Exception e) {
                locationResp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
            }
        return locationResp;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/id/{id}")
    public ResponseEntity<LocationMaster> getLocationById(@PathVariable("id") String id) {
        final String methodName = "getLocationById() : ";
        logger.info(methodName + "called with parameter id ={}",id);
        String status = "active";
        LocationMaster location = null;
        ResponseEntity locationResp = null;
        try {
            location = locationMasterService.getLocationById(id, status);
            if (location != null) {
                locationResp = new ResponseEntity<>(location, HttpStatus.OK);
            } else if (location == null) {
                locationResp = new ResponseEntity<>(new Message("location id does not exist."), HttpStatus.BAD_REQUEST);
            }
            logger.info(methodName + "return with LocationMaster : {} ",location);
            }  catch(ApiException apiException) {
                locationResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch(DataIntegrityViolationException e) {
                locationResp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
            } catch(Exception e) {
                locationResp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
            }
        return locationResp;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/regions")
    public ResponseEntity<?> getRegions()
    {
        final String methodName = "getLocationById() : ";
        logger.info(methodName + "called with parameter empty");
        ResponseEntity resp = null;
        try{
            List<String> list = locationMasterService.getAllRegion("active");
            resp = new ResponseEntity<>(list,HttpStatus.OK);
            logger.info(methodName + "return with LocationMaster list of size : {} ", list.size());
            } catch(ApiException apiException) {
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

    @RequestMapping(method = RequestMethod.GET, value = "/circles/regionname/{regionname}")
    public ResponseEntity<?> getCirclesByRegion(@PathVariable("regionname") String regionName)
    {   final String methodName = "getCirclesByRegion() : ";
        logger.info(methodName + "called with parameter regionName={}",regionName);
        ResponseEntity resp = null;
        try{
            List<String> list = locationMasterService.getAllCircleByRegion(regionName,"active");
            resp = new ResponseEntity<>(list,HttpStatus.OK);
            logger.info(methodName + "return with LocationMaster list of size : {} ", list.size());
            } catch(ApiException apiException) {
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

    @RequestMapping(method = RequestMethod.GET, value = "/divisions/circlename/{circlename}")
    public ResponseEntity<?> getDivisionByCircle(@PathVariable("circlename") String circleName)
    {
        final String methodName = "getDivisionByCircle() : ";
        logger.info(methodName + "called with parameter circleName={}",circleName);
        ResponseEntity resp = null;
        try{
            List<LocationMaster> list = locationMasterService.getAllDivisonByCircle(circleName,"active");
            resp = new ResponseEntity<>(list,HttpStatus.OK);
            logger.info(methodName + "return with LocationMaster list of size : {} ", list.size());
            } catch(ApiException apiException) {
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
}