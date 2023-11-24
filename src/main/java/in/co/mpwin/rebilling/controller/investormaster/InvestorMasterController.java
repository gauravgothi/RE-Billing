package in.co.mpwin.rebilling.controller.investormaster;

import in.co.mpwin.rebilling.beans.investormaster.InvestorMasterBean;
import in.co.mpwin.rebilling.controller.feedermaster.FeederMasterController;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.investormaster.InvestorMasterService;
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
@RequestMapping("/investor")
@CrossOrigin(origins="*")
public class InvestorMasterController {

    private static final Logger logger = LoggerFactory.getLogger(InvestorMasterController.class);

    @Autowired
    InvestorMasterService investorMasterService;

    @RequestMapping(method= RequestMethod.GET,value="")
    public ResponseEntity<?> getAllInvestorMasterBean(){
        final String methodName = "getAllInvestorMasterBean() : ";

        logger.info(methodName + "called. with parameters empty: {}");

        ResponseEntity investorResp = null;
        try {
            String status = "active";
            List<InvestorMasterBean> investorMasterBeanList = investorMasterService.getAllInvestorMasterBean(status);
            if(!investorMasterBeanList.isEmpty())
                investorResp = new ResponseEntity<>(investorMasterBeanList, HttpStatus.OK);
            else if (investorMasterBeanList.isEmpty())
                investorResp = new ResponseEntity<>(new Message("Investor list is not available."),HttpStatus.BAD_REQUEST);

            logger.info(methodName + "return. investorMasterBeanList of size: {} ",investorMasterBeanList.size());
            }catch (ApiException apiException) {
                investorResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch (DataIntegrityViolationException e) {
                investorResp= new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}", e.getMessage());
            } catch (Exception e) {
                investorResp= new ResponseEntity<>(new Message("Exception: " + e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
            }catch(ApiException apiException) {
            investorResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            } catch(DataIntegrityViolationException e) {
            investorResp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
            } catch(Exception e) {
            investorResp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
            }
        return investorResp;
    }

    @RequestMapping(method= RequestMethod.GET,value="/locationId/{locationId}")
    public ResponseEntity<?> getAllInvestorByLocationId(@PathVariable("locationId") String locationId){

        final String methodName = "getAllInvestorByLocationId() : ";
        logger.info(methodName + "called. with parameters locationId: {}",locationId);



        ResponseEntity investorResp = null;
        try {
            String status = "active";
            List<InvestorMasterBean> investorMasterBeanList = investorMasterService.getAllInvestorByLocationId(locationId,status);
            if (!investorMasterBeanList.isEmpty())
                investorResp = new ResponseEntity<>(investorMasterBeanList, HttpStatus.OK);
            else if (investorMasterBeanList.isEmpty())
                investorResp = new ResponseEntity<>(new Message("Investor list is not available."),HttpStatus.BAD_REQUEST);

            logger.info(methodName + "return. investorMasterBeanList of size: {} ",investorMasterBeanList.size());
            }catch (ApiException apiException) {
                investorResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch (DataIntegrityViolationException e) {
                investorResp= new ResponseEntity<>(new Message("Data Integrity Violation : "+e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}", e.getMessage());
            } catch (Exception e) {
                investorResp= new ResponseEntity<>(new Message("Exception: " + e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
            }


            }catch(ApiException apiException) {
            investorResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());

            } catch(DataIntegrityViolationException e) {
                investorResp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);

            } catch(Exception e) {
            investorResp = new ResponseEntity<>(new Message("Exception occurred : " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        return investorResp;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/id/{id}")
    public ResponseEntity<?> getInvestorById(@PathVariable("id") Long id){

        final String methodName = "getInvestorById() : ";
        logger.info(methodName + "called. with parameters id: {}",id);


        String status = "active";
        ResponseEntity investorResp = null;
        try {
            InvestorMasterBean investor = investorMasterService.getInvestorById(id,status);
            if (investor != null)
                investorResp = new ResponseEntity<>(investor, HttpStatus.OK);
             else if (investor == null)
                investorResp = new ResponseEntity<>(new Message(id + " id does not exist."), HttpStatus.BAD_REQUEST);
            } else {
                investorResp = new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }
            logger.info(methodName + "return. InvestorMasterBean investor: {} ",investor.toString());
            } catch (ApiException apiException) {
                investorResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch (DataIntegrityViolationException e) {
                investorResp= new ResponseEntity<>(new Message("Data Integrity Violation : "+e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}", e.getMessage());
            } catch (Exception e) {
                investorResp= new ResponseEntity<>(new Message("Exception: " + e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);


            }catch(ApiException apiException) {
            investorResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            } catch(DataIntegrityViolationException e) {
                investorResp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);

            } catch(Exception e) {
                investorResp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);


            }
        return investorResp;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/investorCode/{investorCode}")

    public ResponseEntity<?> getInvestorById(@PathVariable("investorCode") String investorCode){
        final String methodName = "getInvestorById() : ";
        logger.info(methodName + "called. with parameters investorCode: {}",investorCode);

    public ResponseEntity<?> getInvestorByInvestorCode(@PathVariable("investorCode") String investorCode){

        String status = "active";
        ResponseEntity investorResp = null;
        try {
            InvestorMasterBean investor = investorMasterService.getInvestorByInvestorCode(investorCode,status);
            if (investor != null) {
                investorResp = new ResponseEntity<>(investor, HttpStatus.OK);
            } else if (investor == null) {
                investorResp = new ResponseEntity<>(new Message(investorCode + " code does not exist."), HttpStatus.BAD_REQUEST);
            }

            logger.info(methodName + "return. InvestorMasterBean investor: {} ",investor.toString());
            } catch (ApiException apiException) {
                investorResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch (DataIntegrityViolationException e) {
                investorResp= new ResponseEntity<>(new Message("Data Integrity Violation : "+e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}", e.getMessage());
            } catch (Exception e) {
                investorResp= new ResponseEntity<>(new Message("Exception: " + e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);


            }catch(ApiException apiException) {
            investorResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());

            } catch(DataIntegrityViolationException e) {
                investorResp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);

            } catch(Exception e) {
                investorResp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);


            }
        return investorResp;
    }

    @RequestMapping(method = RequestMethod.POST,value = "")
    public ResponseEntity<?> createInvestorMaster(@Valid @RequestBody InvestorMasterBean investorMasterBean){
        final String methodName = "createInvestorMaster() : ";

        logger.info(methodName + "called. with parameters investorMasterBean: {}",investorMasterBean.toString());

        InvestorMasterBean imb = new InvestorMasterBean();
        ResponseEntity investorInsrtResp = null;
        try {
            imb = investorMasterService.createInvestorMaster(investorMasterBean);
            if(imb!=null)
            {
                investorInsrtResp =  new ResponseEntity<>(new Message(imb.getInvestorCode() + " is created successfully."),HttpStatus.OK);
            }else if(imb==null) {
                investorInsrtResp = new ResponseEntity<>(new Message("investor could not created due to some error"), HttpStatus.BAD_REQUEST);
            }

            logger.info(methodName + "return. InvestorMasterBean investor: {} ",imb.toString());

 
            }catch (ApiException apiException) {
                investorInsrtResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch (DataIntegrityViolationException e) {
                investorInsrtResp= new ResponseEntity<>(new Message("Data Integrity Violation : "+e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}", e.getMessage());
            } catch (Exception e) {
                investorInsrtResp = new ResponseEntity<>(new Message("Exception: " + e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
            }
        return investorInsrtResp;
    }
}
