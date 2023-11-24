package in.co.mpwin.rebilling.controller.mapping;

import in.co.mpwin.rebilling.beans.mapping.InvestorMachineMappingBean;
import in.co.mpwin.rebilling.controller.locationmaster.LocationController;
import in.co.mpwin.rebilling.dto.InvestorMachineMappingDto;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.mapping.InvestorMachineMappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/im/mapping")
@CrossOrigin(origins="*")
public class InvestorMachineMappingController {
    private static final Logger logger = LoggerFactory.getLogger(InvestorMachineMappingController.class);
    @Autowired private InvestorMachineMappingService investorMachineMappingService;

    @RequestMapping(method = RequestMethod.POST,value = "")
    public ResponseEntity<?> createInvestorMachineMapping(@RequestBody InvestorMachineMappingDto investorMachineMappingDto){
        final String methodName = "createInvestorMachineMapping() : ";
        logger.info(methodName + "called with parameter investorMachineMappingDto={}",investorMachineMappingDto);
        ResponseEntity resp = null;
        try {

          String msg  = investorMachineMappingService.createMapping(investorMachineMappingDto);
          resp =  new ResponseEntity<>(new Message(msg), HttpStatus.CREATED);
          logger.info(methodName + "return with message : {} ",msg );

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
    @RequestMapping(method= RequestMethod.GET,value="")
    public ResponseEntity<InvestorMachineMappingBean> getAllMapping(){
        final String methodName = "getAllMapping() : ";
        logger.info(methodName + "called with parameter empty");
        ResponseEntity resp = null;
        try {
            String status = "active";
            List<InvestorMachineMappingBean> mappingList = investorMachineMappingService.getAllMapping(status);
            if(mappingList.size()>0)
            {
                resp = new ResponseEntity<>(mappingList, HttpStatus.OK);
            }
            else if(mappingList.size()==0)
            {
                resp =new ResponseEntity<>(new Message("mapping is not available"),HttpStatus.BAD_REQUEST);
            }
            logger.info(methodName + "return with InvestorMachineMapping list of size : {} ",mappingList.size());
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

    @RequestMapping(method = RequestMethod.GET,value = "/id/{id}")
    public ResponseEntity<?> getMappingById(@PathVariable("id") Long id){
        final String methodName = "getMappingById() : ";
        logger.info(methodName + "called with parameter id={}",id);
        String status = "active";
        ResponseEntity resp = null;
        InvestorMachineMappingBean mappingBean = null;
        try {
            mappingBean = investorMachineMappingService.getMappingById(id,status);
            if ( mappingBean!=null) {
                resp = new ResponseEntity<>( mappingBean, HttpStatus.OK);
            } else if ( mappingBean==null) {
                resp = new ResponseEntity<>(new Message(id + " id does not exist."), HttpStatus.BAD_REQUEST);
            }
            logger.info(methodName + "return with InvestorMachineMappingBean : {} ",mappingBean);

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


    @RequestMapping(method=RequestMethod.GET, value="/mfpId/{mfpId}")
    public ResponseEntity<?> getMappingByMFPId(@PathVariable("mfpId") Long mfpId){
        final String methodName = "getMappingByMFPId() : ";
        logger.info(methodName + "called with parameter mfpId={}",mfpId);
        String status = "active";
        ResponseEntity resp = null;
        List<InvestorMachineMappingBean> mappingBean = null;
        try {
            mappingBean = investorMachineMappingService.getMappingByMFPId(mfpId,status);
            if (mappingBean.size()>0) {
                resp = new ResponseEntity<>( mappingBean, HttpStatus.OK);
            } else if ( mappingBean.size()==0) {
                resp = new ResponseEntity<>(new Message(mfpId + " id does not exist."), HttpStatus.BAD_REQUEST);
            }
            logger.info(methodName + "return with InvestorMachineMappingBean list of size: {} ",mappingBean.size());

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


    @RequestMapping(method=RequestMethod.GET,value="/investorCode/{investorCode}")
    public ResponseEntity<?> getMappingByInvestorCode(@PathVariable("investorCode") String investorCode ){
        final String methodName = "getMappingByInvestorCode() : ";
        logger.info(methodName + "called with parameter investorCode={}",investorCode);
        String status = "active";
        ResponseEntity resp = null;
        List<InvestorMachineMappingBean> mappingBean = null;
        try {
            mappingBean = investorMachineMappingService.getMappingByInvestorCode(investorCode, status);
            if (mappingBean.size()>0) {
                resp = new ResponseEntity<>( mappingBean, HttpStatus.OK);
            } else if (mappingBean.size()==0) {
                resp = new ResponseEntity<>(new Message(investorCode+ " is not exist."), HttpStatus.BAD_REQUEST);
            }
            logger.info(methodName + "return with InvestorMachineMappingBean list of size: {} ",mappingBean.size());
            }catch(ApiException apiException) {
                resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            }catch(DataIntegrityViolationException e) {
                resp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
            }catch(Exception e) {
                resp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
            }
        return resp;
    }
    @RequestMapping(method=RequestMethod.GET,value="/machineCode/{machineCode}")
    public ResponseEntity<?> getMappingByMachineCode(@PathVariable("machineCode") String machineCode ){
        final String methodName = "getMappingByMachineCode() : ";
        logger.info(methodName + "called with parameter machineCode={}",machineCode);
        String status = "active";
        ResponseEntity resp = null;
        List<InvestorMachineMappingBean> mappingBean = null;
        try {
            mappingBean = investorMachineMappingService.getMappingByMachineCode(machineCode,status);
            if (mappingBean.size()>0) {
                resp = new ResponseEntity<>( mappingBean, HttpStatus.OK);
            } else if (mappingBean.size()==0) {
                resp = new ResponseEntity<>(new Message(machineCode+ " is not exist."), HttpStatus.BAD_REQUEST);
            }
            logger.info(methodName + "return with InvestorMachineMappingBean list of size: {} ",mappingBean.size());

            }catch(ApiException apiException) {
                resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            }catch(DataIntegrityViolationException e) {
                resp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
            }catch(Exception e) {
                resp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
            }
        return resp;
    }


}
