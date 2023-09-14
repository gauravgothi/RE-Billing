package in.co.mpwin.rebilling.controller.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterMasterBean;
import in.co.mpwin.rebilling.beans.metermaster.MeterReplacementBean;
import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.dto.MeterReplacementRequest;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.metermaster.MeterMasterService;
import in.co.mpwin.rebilling.services.metermaster.MeterReplacementService;
import in.co.mpwin.rebilling.services.readingservice.MeterReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @RequestMapping(method=RequestMethod.GET, value ="/meterNo/{meterNo}/status/{status}")
    public ResponseEntity<?> GetLastReadingByMeterNo(@PathVariable("meterNo") String meterNo, @PathVariable("status") String status) {

        try {
         MeterReadingBean res =   meterReadingService.GetLastReadingByMeterNoAndStatus(meterNo,status);
         if(res==null)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Message("Last reading not available for meter no. "+meterNo));
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
            String res = meterReplacementService.replaceMeterMethod(meterReplacementRequest.getOldMeterBean(), meterReplacementRequest.getNewMeterBean());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getHttpStatus()).body(new Message(apiException.getMessage()));
        } catch (DataIntegrityViolationException d) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(d.getMessage()));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(e.getMessage()));
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
                resp =new ResponseEntity<>(new Message("Meter replacement data is not available"),HttpStatus.NO_CONTENT);
            }
            else
            {
                resp=new ResponseEntity<>(new Message("something went wrong"),HttpStatus.BAD_REQUEST);
            }

            }  catch (DataIntegrityViolationException d)
        {
            Throwable rootCause = d.getRootCause();
            String msg=rootCause.getMessage().substring(0,rootCause.getMessage().indexOf("Detail:"));
            resp = new ResponseEntity<>(new Message(msg),HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e)
        {
            resp= new ResponseEntity<>(new Message("something went wrong or some exception occurred "+e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return  resp;
    }


}
