package in.co.mpwin.rebilling.controller.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterMasterBean;
import in.co.mpwin.rebilling.controller.mapping.InvestorMachineMappingController;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.metermaster.MeterMasterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/meter")
@CrossOrigin(origins="*")
public class MeterMasterController {

    private static final Logger logger = LoggerFactory.getLogger(MeterMasterController.class);
    @Autowired
    MeterMasterService meterMasterService;

    @RequestMapping(method= RequestMethod.GET,value="/meterno/{meterno}/status/{status}")
    public ResponseEntity<MeterMasterBean> getMeterDetailByMeterNo(@PathVariable("meterno") String meterno,
                                                                    @PathVariable("status") String status) {
        final String methodName = "getMeterDetailByMeterNo() : ";
        logger.info(methodName + "called with parameters meterno={},status={} ",meterno,status);
        ResponseEntity meterDtlResp = null;
        try {

            MeterMasterBean meterMasterBean = new MeterMasterBean();
            meterMasterBean = meterMasterService.getMeterDetailsByMeterNo(meterno, status);
            if(meterMasterBean!=null)
            {
                meterDtlResp = new ResponseEntity<>(meterMasterBean, HttpStatus.OK);
            }
            else if(meterMasterBean==null)
            {
                meterDtlResp=new ResponseEntity<>(new Message("No Record Found"),HttpStatus.BAD_REQUEST);
            }

            logger.info(methodName + "return with MeterMasterBean : {} ",meterMasterBean);
            }catch(ApiException apiException) {
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

    @RequestMapping(method= RequestMethod.GET,value="/status/{status}")
    public ResponseEntity<MeterMasterBean> getAllMeterByStatus(@PathVariable("status") String status) {
        final String methodName = "getAllMeterByStatus() : ";
        logger.info(methodName + "called with parameter status={} ",status);
        ResponseEntity meterDtlResp = null;
        try {
            ArrayList<MeterMasterBean> meterList = meterMasterService.getAllMeterByStatus(status);
            if(meterList.size()>0)
            {
                meterDtlResp = new ResponseEntity<>(meterList, HttpStatus.OK);
            }
            else if(meterList.size()==0)
            {
                meterDtlResp=new ResponseEntity<>(new Message("No Record Found"),HttpStatus.BAD_REQUEST);
            }
            logger.info(methodName + "return with MeterMasterBean list of size : {} ",meterList.size());
            }catch(ApiException apiException) {
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

    @RequestMapping(method= RequestMethod.POST,value="")
    public ResponseEntity<?> createMeterMaster(@Valid @RequestBody MeterMasterBean meterMasterBean) {
        final String methodName = " createMeterMaster() : ";
        logger.info(methodName + "called with parameter meterMasterBean={} ",meterMasterBean);
        //int result = -1;
        //String resp = null;
        MeterMasterBean mmb = new MeterMasterBean();
        //mmb = null;
        ResponseEntity meterInsrtResp = null;
        try {
            mmb = meterMasterService.createMeterMaster(meterMasterBean);
            if (mmb != null) {
               meterInsrtResp = new ResponseEntity<>(new Message(mmb.getMeterNumber() + " is created successfully."), HttpStatus.OK);
            }
            logger.info(methodName + "return with MeterMasterBean : {} ",mmb);
            }catch(ApiException apiException) {
                meterInsrtResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch(DataIntegrityViolationException e) {
                meterInsrtResp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
            } catch(Exception e) {
                meterInsrtResp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
            }
        return meterInsrtResp;
    }

    @GetMapping(value = "/list/byUser")
    public ResponseEntity<?> getMetersByUser(){
        final String methodName = "getMetersByUser() : ";
        logger.info(methodName + "called with parameter empty ");
        //int result = -1;
        ResponseEntity meterListResp  = null;
        try {
                List<Map<String,String>> meterList = meterMasterService.getMetersByUser();
                meterListResp = new ResponseEntity<>(meterList,HttpStatus.OK);
            logger.info(methodName + "return with MeterMasterBean list of size : {} ",meterList.size());
        }catch(ApiException apiException) {
            meterListResp  = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch(DataIntegrityViolationException e) {
            meterListResp  = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
        } catch(Exception e) {
            meterListResp  = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
        }
        return meterListResp;
    }

    @GetMapping(value = "/list")
    public ResponseEntity<?> getAllMeters(){
        final String methodName = "getAllMeters() : ";
        logger.info(methodName + "called with parameter empty ");
        ResponseEntity meterListResp  = null;
        try {
            List<Map<String,String>> meterList = meterMasterService.getMeters();
            meterListResp = new ResponseEntity<>(meterList,HttpStatus.OK);
            logger.info(methodName + "return with MeterMasterBean list of size : {} ",meterList.size());
        }catch(ApiException apiException) {
            meterListResp  = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch(DataIntegrityViolationException e) {
            meterListResp  = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
        } catch(Exception e) {
            meterListResp  = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
        }
        return meterListResp;
    }
 // get list of meter bean where status = active and  is_mapped = no for mfp mapping
    @GetMapping(value = "/list/unmapped/category/{category}")
    public ResponseEntity<?> getUnmappedMeterBeansByCategory(@PathVariable("category") String category){
        final String methodName = "getUnmappedMeterBeansByCategory() : ";
        logger.info(methodName + "called with parameter category={}",category);

        ResponseEntity meterListResp  = null;
        try {
            List<MeterMasterBean> meterList = meterMasterService.getUnmappedMeterBeans("active","no",category);
            if(meterList.isEmpty())
                throw new ApiException(HttpStatus.BAD_REQUEST,"active and unmapped meter list not found in meter master");
            meterListResp = new ResponseEntity<>(meterList,HttpStatus.OK);
            logger.info(methodName + "return with MeterMasterBean list of size : {} ",meterList.size());
        }catch(ApiException apiException) {
            meterListResp  = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch(DataIntegrityViolationException e) {
            meterListResp  = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
        } catch(Exception e) {
            meterListResp  = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
        }
        return meterListResp;
    }
}
