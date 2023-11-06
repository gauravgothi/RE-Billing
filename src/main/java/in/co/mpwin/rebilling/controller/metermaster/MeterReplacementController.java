package in.co.mpwin.rebilling.controller.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterMasterBean;
import in.co.mpwin.rebilling.beans.metermaster.MeterReplacementBean;
import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.beans.thirdparty.ThirdPartyBean;
import in.co.mpwin.rebilling.dto.MeterReplacementRequest;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.metermaster.MeterMasterService;
import in.co.mpwin.rebilling.services.metermaster.MeterReplacementService;
import in.co.mpwin.rebilling.services.readingservice.MeterReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/meter/replace")
@CrossOrigin(origins="*")
public class MeterReplacementController {
    @Autowired
    MeterReplacementService meterReplacementService;

    @Autowired
    MeterReadingService meterReadingService;
    @Autowired
    MeterMasterService meterMasterService;

    @RequestMapping(method=RequestMethod.GET, value ="/last/read/meternumber/{meternumber}")
    public ResponseEntity<?> GetLastReadingByMeterNo(@PathVariable("meternumber") String meterNo) {

        try {
         MeterReadingBean res =   meterReadingService.GetLastReadingByMeterNoAndStatus(meterNo,"active");
         if(res==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message("Last reading not available for meter no. "+meterNo));
         else
             return ResponseEntity.status(HttpStatus.OK).body(res);
            }catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(ex.getMessage()));
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "")
    public ResponseEntity<?> meterReplacement(@RequestBody MeterReplacementRequest meterReplacementRequest) {
        try {
            Boolean resp = meterReplacementService.replaceMeterMethod(meterReplacementRequest.getOldMeterBean(), meterReplacementRequest.getNewMeterBean());
            if(resp)
            return ResponseEntity.status(HttpStatus.OK).body("Meter replacement done.");
            else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong.");
        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getHttpStatus()).body(new Message(apiException.getMessage()));
        } catch (DataIntegrityViolationException d) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(d.getMessage().substring(0,250)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(e.getMessage().substring(0,250)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(e.getMessage().substring(0,250)));
        }
    }
    @RequestMapping(method= RequestMethod.GET,value="/category/{category}/status/{status}/mapped/{mapped}")
    public ResponseEntity<MeterMasterBean> getMeterDetailsByCategory(@PathVariable("category") String category,
                                                                     @PathVariable("status") String status, @PathVariable("mapped") String mapped) {
        ResponseEntity meterDtlResp = null;
        try {

            List<MeterMasterBean> meterMasterBean = new ArrayList<>();
            meterMasterBean = meterMasterService.getMeterDetailsByCategory(category,status,mapped);
            if(!meterMasterBean.isEmpty())
            {
                meterDtlResp = new ResponseEntity<>(meterMasterBean, HttpStatus.OK);
            }
            else if(meterMasterBean.size()==0)
            {
                meterDtlResp=new ResponseEntity<>(new Message("Meter Details not present for meter category "+category),HttpStatus.NO_CONTENT);
            }
            else
            {
                meterDtlResp=new ResponseEntity<>(new Message("something went wrong"),HttpStatus.BAD_REQUEST);
            }
        } catch (DataIntegrityViolationException d)
        {
            Throwable rootCause = d.getRootCause();
            String msg=rootCause.getMessage().substring(0,rootCause.getMessage().indexOf("Detail:"));
            meterDtlResp = new ResponseEntity<>(new Message(msg),HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e)
        {
            meterDtlResp = new ResponseEntity<>(new Message("something went wrong or some exception occurred "+e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return meterDtlResp;
    }

    @RequestMapping(method= RequestMethod.GET,value="/list/status/{status}")
    public ResponseEntity<?> getMeterReplacementList(@PathVariable String status)
    {   ResponseEntity resp= null;
        try {
        List<MeterReplacementBean> replacementList = meterReplacementService.getMeterReplacementList(status);
            if(!replacementList.isEmpty())
            {
                resp = new ResponseEntity<>(replacementList, HttpStatus.OK);
            }
            else if(replacementList.size()==0)
            {
                resp =new ResponseEntity<>(new Message("Meter replacement data is not available"),HttpStatus.BAD_REQUEST);
            }
            else
            {
                resp=new ResponseEntity<>(new Message("something went wrong"),HttpStatus.BAD_REQUEST);
            }

            }  catch (DataIntegrityViolationException d)
        {
            Throwable rootCause = d.getRootCause();
            String msg=rootCause.getMessage().substring(0,rootCause.getMessage().indexOf("Detail:"));
            resp = new ResponseEntity<>(new Message(msg),HttpStatus.BAD_REQUEST);
        } catch (Exception e)
        {
            resp= new ResponseEntity<>(new Message("something went wrong or some exception occurred "+e.getMessage()),HttpStatus.BAD_REQUEST);

        }
        return  resp;
    }

    @RequestMapping(method=RequestMethod.GET, value ="/meterNo/{meterNo}/month/{month}")
    public ResponseEntity<?> GetLastReadingByMeterNoAndMonth(@PathVariable("meterNo") String meterNo, @PathVariable("month") String month) {

        try {    //
            Date readingPunchDate = new DateMethods().getCurrentAndPreviousDate(month).get(1);
            MeterReadingBean res = meterReadingService.GetLastReadingByMeterNoAndStatus(meterNo,readingPunchDate);
            if(res==null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message("You can not punch reading. because SR reading is not available for meter no. "+meterNo));
            else
                return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getHttpStatus()).body(new Message(apiException.getMessage()));
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(ex.getMessage()));
        }catch (ParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message("Date parse error : Month should be in MMM-yyyy."));
        }
        catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(ex.getMessage()));
        }

    }
    @RequestMapping(method=RequestMethod.GET, value ="/old/meters")
    public ResponseEntity<?> GetInstalledMeterListForReplacement()  {
        ResponseEntity response =null;

        try {
            List<MeterMasterBean> mappedMeterList = meterReplacementService.getMappedMeterBeansByMfpMappingBean();
            response = new ResponseEntity<>(mappedMeterList, HttpStatus.OK);
        } catch (ApiException apiException) {
            response = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {

            response = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, 200)), HttpStatus.BAD_REQUEST);
        }
        return response;

    }

    @RequestMapping(method=RequestMethod.GET, value ="/new/meters")
    public ResponseEntity<?> GetNewMeterListForReplacement()  {
        ResponseEntity response =null;
        try {
            List<MeterMasterBean> unMappedMeterList = meterMasterService.getMeterByStatusAndIsMappped("active","no");
            response = new ResponseEntity<>(unMappedMeterList, HttpStatus.OK);
        } catch (ApiException apiException) {
            response = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            response = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, 200)), HttpStatus.BAD_REQUEST);
        }
        return response;
    }


    @RequestMapping(method=RequestMethod.GET, value ="/old/meternumber/{meternumber}")
    public ResponseEntity<?> GetOldMeterForReplacement(@PathVariable("meternumber") String meternumber)  {
        ResponseEntity response =null;

        try {
            MeterMasterBean mappedMeter = meterReplacementService.getMappedMeterBeanForReplacement(meternumber);
            response = new ResponseEntity<>(mappedMeter, HttpStatus.OK);
        } catch (ApiException apiException) {
            response = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {

            response = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, 200)), HttpStatus.BAD_REQUEST);
        }
        return response;

    }

}
