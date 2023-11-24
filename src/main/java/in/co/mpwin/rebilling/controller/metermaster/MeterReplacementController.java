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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(MeterReplacementController.class);
    @Autowired
    MeterReplacementService meterReplacementService;

    @Autowired
    MeterReadingService meterReadingService;
    @Autowired
    MeterMasterService meterMasterService;

    @RequestMapping(method=RequestMethod.GET, value ="/last/read/meternumber/{meternumber}")
    public ResponseEntity<?> GetLastReadingByMeterNo(@PathVariable("meternumber") String meterNo) {
        final String methodName = "GetLastReadingByMeterNo() : ";
        logger.info(methodName + "called with parameters meterno={}",meterNo);
        ResponseEntity resp = null;
        try {
             MeterReadingBean res =   meterReadingService.GetLastReadingByMeterNoAndStatus(meterNo,"active");
             if(res==null)
                resp = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message("Last reading not available for meter no. "+meterNo));
             else
             resp =  ResponseEntity.status(HttpStatus.OK).body(res);

            logger.info(methodName + "return with MeterReadingBean : {} ",res);
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

    @RequestMapping(method = RequestMethod.POST, value = "")
    public ResponseEntity<?> meterReplacement(@RequestBody MeterReplacementRequest meterReplacementRequest) {
        final String methodName = "meterReplacement() : ";
        logger.info(methodName + "called with parameters meterReplacementRequest={}",meterReplacementRequest);
        ResponseEntity resp = null;
        try {
            Boolean result = meterReplacementService.replaceMeterMethod(meterReplacementRequest.getOldMeterBean(), meterReplacementRequest.getNewMeterBean());
            if(result)
            resp = ResponseEntity.status(HttpStatus.OK).body(new Message("Meter replacement done."));
            else
            resp = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message("Something went wrong."));
            logger.info(methodName + "return with result : {} ",result);

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
    @RequestMapping(method= RequestMethod.GET,value="/category/{category}/status/{status}/mapped/{mapped}")
    public ResponseEntity<MeterMasterBean> getMeterDetailsByCategory(@PathVariable("category") String category,
                                                                     @PathVariable("status") String status, @PathVariable("mapped") String mapped) {
        final String methodName = "getMeterDetailsByCategory() : ";
        logger.info(methodName + "called with parameters category={}, mapped={}, status={} ",category,mapped,status);
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
                    meterDtlResp=new ResponseEntity<>(new Message("Meter Details not present for meter category "+category),HttpStatus.BAD_REQUEST);
                }
                else
                {
                    meterDtlResp=new ResponseEntity<>(new Message("something went wrong"),HttpStatus.BAD_REQUEST);
                }
                logger.info(methodName + "return with MeterMasterBean list of size : {} ",meterMasterBean.size());
                } catch(ApiException apiException) {
                meterDtlResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                    logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
                } catch(DataIntegrityViolationException e) {
                meterDtlResp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
                    logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
                } catch(Exception e) {
                meterDtlResp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
                    logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
                }
        return meterDtlResp;
    }

    @RequestMapping(method= RequestMethod.GET,value="/list/status/{status}")
    public ResponseEntity<?> getMeterReplacementList(@PathVariable String status)
    {
        final String methodName = "getMeterReplacementList() : ";
        logger.info(methodName + "called with parameters status={} ",status);
        ResponseEntity resp= null;
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

            logger.info(methodName + "return with MeterReplacementBean list of size : {} ",replacementList.size());

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
        return  resp;
    }

    @RequestMapping(method=RequestMethod.GET, value ="/meterNo/{meterNo}/month/{month}")
    public ResponseEntity<?> GetLastReadingByMeterNoAndMonth(@PathVariable("meterNo") String meterNo, @PathVariable("month") String month) {
        final String methodName = "GetLastReadingByMeterNoAndMonth() : ";
        logger.info(methodName + "called with parameters meterNo={}, month={} ",meterNo,month);
        ResponseEntity resp= null;
        try {    //
            Date readingPunchDate = new DateMethods().getCurrentAndPreviousDate(month).get(1);
            MeterReadingBean lastReadBean = meterReadingService.GetLastReadingByMeterNoAndStatus(meterNo,readingPunchDate);
            if(lastReadBean==null)
                resp = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message("You can not punch reading. because SR reading is not available for meter no. "+meterNo));
            else
                resp = ResponseEntity.status(HttpStatus.OK).body(lastReadBean);
            logger.info(methodName + "return with  last meter reading bean : {} ",lastReadBean);
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
    @RequestMapping(method=RequestMethod.GET, value ="/old/meters")
    public ResponseEntity<?> GetInstalledMeterListForReplacement()  {
        final String methodName = "GetInstalledMeterListForReplacement() : ";
        logger.info(methodName + "called with parameters empty ");

        ResponseEntity resp =null;

        try {
            List<MeterMasterBean> mappedMeterList = meterReplacementService.getMappedMeterBeansByMfpMappingBean();
            resp = new ResponseEntity<>(mappedMeterList, HttpStatus.OK);
            logger.info(methodName + "return with MeterMasterBean list of size : {} ",mappedMeterList.size());
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

    @RequestMapping(method=RequestMethod.GET, value ="/new/meters")
    public ResponseEntity<?> GetNewMeterListForReplacement()  {
        final String methodName = "GetNewMeterListForReplacement() : ";
        logger.info(methodName + "called with parameters empty ");
        ResponseEntity resp =null;
        try {
            List<MeterMasterBean> unMappedMeterList = meterMasterService.getMeterByStatusAndIsMappped("active","no");
            resp = new ResponseEntity<>(unMappedMeterList, HttpStatus.OK);
            logger.info(methodName + "return with un mapped MeterMasterBean list of size : {} ",unMappedMeterList.size());
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


    @RequestMapping(method=RequestMethod.GET, value ="/old/meternumber/{meternumber}")
    public ResponseEntity<?> GetOldMeterForReplacement(@PathVariable("meternumber") String meternumber)  {
        final String methodName = "GetOldMeterForReplacement() : ";
        logger.info(methodName + "called with parameters meter number ={} ",meternumber);
        ResponseEntity resp =null;
        try {
            MeterMasterBean mappedMeter = meterReplacementService.getMappedMeterBeanForReplacement(meternumber);
            resp = new ResponseEntity<>(mappedMeter, HttpStatus.OK);
            logger.info(methodName + "return with mapped MeterMasterBean : {} ",mappedMeter);
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
