package in.co.mpwin.rebilling.controller.readingcontroller;

import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.readingservice.MeterReadingService;
import in.co.mpwin.rebilling.services.readingservice.MeterReadingPunchingService;
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


    @Autowired
    MeterReadingService meterReadingService;


    @Autowired
    MeterReadingPunchingService meterReadingPunchingService;

    @RequestMapping(method = RequestMethod.POST, value = "")
    public ResponseEntity<?> saveReading(@RequestBody MeterReadingBean meterReadingBean) {
        ResponseEntity res = null;
        try {
            MeterReadingBean mrb = meterReadingPunchingService.saveMeterReading(meterReadingBean);
            if(mrb!=null)
                res = new ResponseEntity<>(new Message("Reading saved successfully."), HttpStatus.OK);
            if(mrb==null)
                res = new ResponseEntity<>(new Message("Reading not saved due to some error."),HttpStatus.BAD_REQUEST);
        }catch (ApiException apiException){
            res = new ResponseEntity<>(apiException.getMessage(),apiException.getHttpStatus());
        }catch (DataIntegrityViolationException d)
        {
            Throwable rootCause = d.getRootCause();
            String msg=rootCause.getMessage().substring(0,rootCause.getMessage().indexOf("Detail:"));
           res = new ResponseEntity<>(new Message(msg),HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(NullPointerException ex)
        {
            String msg=ex.getMessage().substring(0,ex.getMessage().indexOf("Detail:"));
            res = new ResponseEntity<>(new Message(msg),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (Exception e){
            e.printStackTrace();
            res = new ResponseEntity<>(new Message("something went wrong or some exception occurred "),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return res;
    }



}
