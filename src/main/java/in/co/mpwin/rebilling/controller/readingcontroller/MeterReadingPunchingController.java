package in.co.mpwin.rebilling.controller.readingcontroller;

import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.readingservice.MeterReadingService;
import in.co.mpwin.rebilling.services.readingservice.MeterReadingPunchingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meter_reading/punch")
@CrossOrigin(origins="*")
public class MeterReadingPunchingController {


    private static final Logger logger = LoggerFactory.getLogger(MeterReadingPunchingController.class);

    @Autowired
    MeterReadingService meterReadingService;


    @Autowired
    MeterReadingPunchingService meterReadingPunchingService;

    @RequestMapping(method = RequestMethod.POST, value = "")
    public ResponseEntity<?> saveReading(@RequestBody MeterReadingBean meterReadingBean) {
        final String methodName = "saveReading() : ";
        logger.info(methodName + "called. with request body of meterReadingBean: {}",meterReadingBean.toString());
        ResponseEntity res = null;
        try {
            MeterReadingBean mrb = meterReadingPunchingService.saveMeterReading(meterReadingBean);
            if(mrb!=null) {
                res = new ResponseEntity<>(new Message("Reading saved successfully."), HttpStatus.OK);
                logger.info(methodName + "return. success response : {}",res.getBody());
            }
            if(mrb==null) {
                res = new ResponseEntity<>(new Message("Reading not saved due to some error."), HttpStatus.BAD_REQUEST);
                logger.info(methodName + "return.fail response message : {}",res.getBody());
            }
        }catch (ApiException apiException){
            res = new ResponseEntity<>(apiException.getMessage(),apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        }catch (DataIntegrityViolationException d) {
            res = new ResponseEntity<>(new Message("DataIntegrityViolationException occured :"+ d.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
        } catch(NullPointerException ex) {
            res = new ResponseEntity<>(new Message(ex.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" NullPointerException occurred: {}", ex.getMessage());
        } catch (Exception ex){
            res = new ResponseEntity<>(new Message("some exception occurred: "+ex.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", ex.getMessage(),ex);
        }
        return res;
    }





}
