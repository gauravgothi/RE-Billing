package in.co.mpwin.rebilling.controller.thirdparty;

import in.co.mpwin.rebilling.beans.investormaster.InvestorMasterBean;
import in.co.mpwin.rebilling.beans.thirdparty.DeveloperPlantDto;
import in.co.mpwin.rebilling.beans.thirdparty.ThirdPartyBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.mapping.MeterFeederPlantMappingService;
import in.co.mpwin.rebilling.services.thirdparty.ThirdPartyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/thirdparty")
@CrossOrigin(origins="*")
public class ThirdPartyController {
    private static final Logger logger = LoggerFactory.getLogger(ThirdPartyController.class);
    @Autowired private ThirdPartyService thirdPartyService;

    @Autowired private MeterFeederPlantMappingService meterFeederPlantMappingService;

    // save third party bean into database
    @PostMapping("/save")
    public ResponseEntity<?> saveThirdPartyBean(@RequestBody ThirdPartyBean tpBean) {
        final String methodName = "saveThirdPartyBean() : ";
        logger.info(methodName + "called. ThirdPartyBean: {} ",tpBean);

        ResponseEntity resp = null;
        try {
            ThirdPartyBean saveBean = thirdPartyService.saveThirdPartyBean(tpBean);
            resp = new ResponseEntity<>(saveBean, HttpStatus.OK);
            logger.info(methodName + "return. ThirdPartyBean saved successfully : {} ",saveBean);
            } catch (ApiException apiException) {
                resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch (DataIntegrityViolationException e) {
                resp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}", e.getMessage());
            } catch (Exception e) {
                resp = new ResponseEntity<>(new Message("Exception occurred : " + e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
            }
        return resp;
    }

    // get active third party list

    @GetMapping("/list")
    public ResponseEntity<?> getThirdPartyBeans() {
        final String methodName = "getThirdPartyBeans() : ";
        logger.info(methodName + "called.");
        ResponseEntity resp = null;
        try {
            List<ThirdPartyBean> tpLists = thirdPartyService.getThirdPartyBeans();
            resp = new ResponseEntity<>(tpLists, HttpStatus.OK);
                logger.info(methodName + "return thirdparty successfully of size : {} ",tpLists.size());
            }catch (ApiException apiException) {
                resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch (DataIntegrityViolationException e) {
                resp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}", e.getMessage());
            } catch (Exception e) {
                resp = new ResponseEntity<>(new Message("Exception occurred : " + e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
            }
        return resp;
    }

    @GetMapping("/list/status/{status}")
    public ResponseEntity<?> getThirdPartyBeansByStatus(@PathVariable("status") String status) {
        final String methodName = "getThirdPartyBeansByStatus() : ";
        logger.info(methodName + "called with parameter status={}",status);
        ResponseEntity resp = null;
        try {
            List<ThirdPartyBean> tpLists = thirdPartyService.getThirdPartyBeansByStatus(status);
            resp = new ResponseEntity<>(tpLists, HttpStatus.OK);
            logger.info(methodName + "return thirdparty successfully of size : {} ",tpLists.size());
            }catch (ApiException apiException) {
                resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch (DataIntegrityViolationException e) {
                resp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}", e.getMessage());
            } catch (Exception e) {
                resp = new ResponseEntity<>(new Message("Exception occurred : " + e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
            }
        return resp;
    }
    @GetMapping("/dto/developerId/{developerId}/plantCode/{plantCode}")
    public ResponseEntity<?> getMfpMapping(@PathVariable("developerId") String developerId, @PathVariable("plantCode") String plantCode)
    {
        final String methodName = "getMfpMapping() : ";
        logger.info(methodName + "called with parameters developerId={}, plantCode={} ",developerId,plantCode);
        ResponseEntity resp = null;
        try {
            DeveloperPlantDto developerPlantDto = thirdPartyService.getDeveloperPlantDto(developerId,plantCode);
            resp = new ResponseEntity<>(developerPlantDto, HttpStatus.OK);
            logger.info(methodName + "return developerPlantDto successfully : {} ",developerPlantDto);
            } catch (ApiException apiException) {
                resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch (DataIntegrityViolationException e) {
                resp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}", e.getMessage());
            } catch (Exception e) {
                resp = new ResponseEntity<>(new Message("Exception occurred : " + e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
            }
        return resp;
    }


    @GetMapping("/list/consumerCode/{consumerCode}")
    public ResponseEntity<?>  getThirdPartyBeansByConsumerCode(@PathVariable String consumerCode) {
        final String methodName = "getThirdPartyBeansByConsumerCode() : ";
        logger.info(methodName + "called with parameter consumerCode={}",consumerCode);
        ResponseEntity resp = null;
        try {
            List<ThirdPartyBean> tpLists = thirdPartyService.getThirdPartyByConsumerCode(consumerCode,"active");
            resp = new ResponseEntity<>(tpLists, HttpStatus.OK);
            logger.info(methodName + "return third party list of size : {} ",tpLists.size());
            } catch (ApiException apiException) {
                resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch (DataIntegrityViolationException e) {
                resp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}", e.getMessage());
            } catch (Exception e) {
                resp = new ResponseEntity<>(new Message("Exception occurred : " + e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
            }
        return resp;
    }

    @GetMapping("/list/investorCode/{investorCode}")
    public ResponseEntity<?>  getThirdPartyBeansByInvestorId(@PathVariable String investorCode) {
        final String methodName = "getThirdPartyBeansByInvestorId() : ";
        logger.info(methodName + "called with parameter investorCode={}",investorCode);
        ResponseEntity resp = null;
        try {
            List<ThirdPartyBean> tpLists = thirdPartyService.getThirdPartiesByInvestorId(investorCode,"active");
            resp = new ResponseEntity<>(tpLists, HttpStatus.OK);
            logger.info(methodName + "return third party list of size : {} ",tpLists.size());
            } catch (ApiException apiException) {
                resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch (DataIntegrityViolationException e) {
                resp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}", e.getMessage());
            } catch (Exception e) {
                resp = new ResponseEntity<>(new Message("Exception occurred : " + e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
            }
        return resp;
    }

    @GetMapping("/list/plantCode/{plantCode}")
    public ResponseEntity<?>  getThirdPartyBeansByPlantCode(@PathVariable String plantCode) {
        final String methodName = "getThirdPartyBeansByPlantCode() : ";
        logger.info(methodName + "called with parameter plantCode={}",plantCode);
        ResponseEntity resp = null;
        try {
            List<ThirdPartyBean> tpLists = thirdPartyService.getThirdPartiesByPlantCode(plantCode,"active");
            resp = new ResponseEntity<>(tpLists, HttpStatus.OK);
            logger.info(methodName+"return third party list of size : {} ",tpLists.size());
            } catch (ApiException apiException) {
                resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch (DataIntegrityViolationException e) {
                resp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}", e.getMessage());
            } catch (Exception e) {
                resp = new ResponseEntity<>(new Message("Exception occurred : " + e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
            }
        return resp;
    }

    @GetMapping("/investors/developerId/{developerId}/plantCode/{plantCode}")
    public ResponseEntity<?> getInvestors(@PathVariable("developerId") String developerId, @PathVariable("plantCode") String plantCode)
    {
        final String methodName = "getInvestors() : ";
        logger.info(methodName + "called with parameters developerId={}, plantCode={} ",developerId,plantCode);
        ResponseEntity resp = null;
        try {
            List<InvestorMasterBean> investors = thirdPartyService.getInvestorsByDeveloperIdAndPlantCode(developerId,plantCode);
            resp = new ResponseEntity<>(investors, HttpStatus.OK);
            logger.info(methodName+"return investor beans list of size : {} ",investors.size());
            } catch (ApiException apiException) {
                resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch (DataIntegrityViolationException e) {
                resp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}", e.getMessage());
            } catch (Exception e) {
                resp = new ResponseEntity<>(new Message("Exception occurred : " + e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
            }
        return resp;
    }

    @PutMapping("/inactive")
    public ResponseEntity<?> setThirdPartyInactive(@RequestBody ThirdPartyBean thirdParty)
    {
        final String methodName = "setThirdPartyInactive() : ";
        logger.info(methodName + "called with parameters thirdParty={}",thirdParty);
        ResponseEntity resp = null;
        try {
            ThirdPartyBean tpBean = thirdPartyService.setThirdPartyInactive(thirdParty);
            resp = new ResponseEntity<>(tpBean,HttpStatus.OK);
            logger.info(methodName + "return with ThirdPartyBean set inactive successfully : {} ",tpBean);
            }catch (ApiException apiException) {
                resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch (DataIntegrityViolationException e) {
                resp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}", e.getMessage());
            } catch (Exception e) {
                resp = new ResponseEntity<>(new Message("Exception occurred : " + e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
            }
        return resp;
    }

    @PutMapping("/active")
    public ResponseEntity<?> setThirdPartyActive(@RequestBody ThirdPartyBean thirdParty)
    {
        final String methodName = "setThirdPartyActive() : ";
        logger.info(methodName + "called with parameters thirdParty={}",thirdParty);
        ResponseEntity resp = null;
        try {
            ThirdPartyBean tpBean = thirdPartyService.setThirdPartyActive(thirdParty);
            resp = new ResponseEntity<>(tpBean,HttpStatus.OK);
            logger.info(methodName + "return with ThirdPartyBean set active successfully : {} ",tpBean);
            }catch (ApiException apiException) {
                resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch (DataIntegrityViolationException e) {
                resp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}", e.getMessage());
            } catch (Exception e) {
                resp = new ResponseEntity<>(new Message("Exception occurred : " + e.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
            }
        return resp;
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateThirdPartyInactive(@RequestBody ThirdPartyBean thirdParty)
    {
        final String methodName = "updateThirdPartyInactive() : ";
        logger.info(methodName + "called with parameters thirdParty={}",thirdParty);
        ResponseEntity resp = null;
        try {
            ThirdPartyBean tpBean = thirdPartyService.updateThirdParty(thirdParty);
            resp = new ResponseEntity<>(tpBean,HttpStatus.OK);
            logger.info(methodName + "return with ThirdPartyBean set inactive successfully : {} ",tpBean);
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
}
