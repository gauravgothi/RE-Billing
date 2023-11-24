package in.co.mpwin.rebilling.controller.bifurcatecontroller;

import in.co.mpwin.rebilling.beans.bifurcation.BifurcateBean;
import in.co.mpwin.rebilling.dto.BifurcateConsumptionDto;
import in.co.mpwin.rebilling.dto.MeterConsumptionDto;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.bifurcateservice.BifurcateConsumptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bifurcate")
@CrossOrigin(origins="*")
public class BifurcatedController {

    private static final Logger logger = LoggerFactory.getLogger(BifurcatedController.class);

    @Autowired
    private BifurcateConsumptionService bifurcateService;

    //This is used by developer to bifurcate the meter consumption
    @PostMapping("/empty")
    public ResponseEntity<?> getConsumptionBifurcateDto(@RequestBody MeterConsumptionDto dto) {
        final String methodName = "getConsumptionBifurcateDto() : ";
        logger.info(methodName + "called. with parameters meterConsumptionDto: {} ",dto.toString());

        ResponseEntity bifurcateResp = null;
        try {
            BifurcateConsumptionDto bifurcateConsumptionDto = bifurcateService.getBifurcateDto(dto);
            bifurcateResp = new ResponseEntity<>(bifurcateConsumptionDto, HttpStatus.OK);
            logger.info(methodName + "return. bifurcateConsumptionDto saved successfully : {} ",bifurcateConsumptionDto.toString());
        } catch (ApiException apiException) {
            bifurcateResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch (Exception e) {
            bifurcateResp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return bifurcateResp;
    }

    @PostMapping("")
    public ResponseEntity<?> saveConsumptionBifurcateDto(@RequestBody BifurcateConsumptionDto dto) {
        final String methodName = "saveConsumptionBifurcateDto() : ";
        logger.info(methodName + "called. with parameters bifurcateConsumptionDto: {} ",dto.toString());
        ResponseEntity saveBifurcateResp = null;
        try {
            BifurcateConsumptionDto bifurcatedBean = bifurcateService.saveBifurcateDto(dto);
            saveBifurcateResp = new ResponseEntity<>(bifurcatedBean, HttpStatus.OK);
            logger.info(methodName + "return. bifurcateConsumptionDto saved successfully : {} ",bifurcatedBean.toString());
        } catch (ApiException apiException) {
            saveBifurcateResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch (DataIntegrityViolationException d) {
            saveBifurcateResp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
        } catch (Exception e) {
            saveBifurcateResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return saveBifurcateResp;
    }

    //get investor list associated with developer only on invoice generation page
    @GetMapping("/investor-list-lov")
    public ResponseEntity<?> getInvestorsByDeveloper() {
        final String methodName = "getInvestorsByDeveloper() : ";
        logger.info(methodName + "called. with parameters empty: ");
        ResponseEntity investorListResp = null;
        try {
            List<Map<String,String>> investorList = bifurcateService.getInvestorListByDeveloperId();
            investorListResp = new ResponseEntity<>(investorList, HttpStatus.OK);
            logger.info(methodName + "return. investor list of size: {} ",investorList.size());
        } catch (ApiException apiException) {
            investorListResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch (DataIntegrityViolationException d) {
            investorListResp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
        } catch (Exception e) {
            investorListResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return investorListResp;
    }

    //get meter list associated with developer only on invoice generation page
    @GetMapping("/developer/meters")
    public ResponseEntity<?> getMetersByDeveloper() {
        final String methodName = "getMetersByDeveloper() : ";
        logger.info(methodName + "called. with parameters empty: ");
        ResponseEntity meterListResp = null;
        try {
            List<Map<String,String>> meterList = bifurcateService.getMeterListByDeveloperId();
            meterListResp = new ResponseEntity<>(meterList, HttpStatus.OK);
            logger.info(methodName + "return. meter list of size: {} ",meterList.size());
        } catch (ApiException apiException) {
            meterListResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch (DataIntegrityViolationException d) {
            meterListResp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
        } catch (Exception e) {
            meterListResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return meterListResp;
    }

    //get meter list associated with circle only on invoice generation page from bifurcated table
    @GetMapping("/circle/meters")
    public ResponseEntity<?> getMetersByCircle() {
        final String methodName = "getMetersByCircle() : ";
        logger.info(methodName + "called. with parameters empty: ");
        ResponseEntity meterListResp = null;
        try {
            List<Map<String,String>> meterList = bifurcateService.getMeterListByCircleName();
            meterListResp = new ResponseEntity<>(meterList, HttpStatus.OK);
            logger.info(methodName + "return. meter list of size: {} ",meterList.size());
        } catch (ApiException apiException) {
            meterListResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch (DataIntegrityViolationException d) {
            meterListResp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
        } catch (Exception e) {
            meterListResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return meterListResp;
    }

    //get all meter list only on invoice generation page from bifurcated table
    @GetMapping("/all/meters")
    public ResponseEntity<?> getAllMeters() {
        final String methodName = "getAllMeters() : ";
        logger.info(methodName + "called. with parameters empty: ");
        ResponseEntity meterListResp = null;
        try {
            List<Map<String,String>> meterList = bifurcateService.getAllMeters();
            meterListResp = new ResponseEntity<>(meterList, HttpStatus.OK);
            logger.info(methodName + "return. meter list of size: {} ",meterList.size());
        } catch (ApiException apiException) {
            meterListResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch (DataIntegrityViolationException d) {
            meterListResp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
        } catch (Exception e) {
            meterListResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return meterListResp;
    }

    //get bifurcation dto of already bifurcated bean of meter
    @GetMapping("/get/dto/meterNo/{meterNo}/monthYear/{monthYear}")
    public ResponseEntity<?> getAlreadyBifurcatedBeanDto(@PathVariable("meterNo") String meterNo,@PathVariable("monthYear") String monthYear) {
        final String methodName = "getAlreadyBifurcatedBeanDto() : ";
        logger.info(methodName + "called. with parameters meterNo: {}, monthYear: {}",meterNo,monthYear);
        ResponseEntity alreadyBifurcatedBeanDto = null;
        try {
            BifurcateConsumptionDto bifurcateConsumptionDto = bifurcateService.getAlreadyBifurcatedBeanDto(meterNo,monthYear);
            alreadyBifurcatedBeanDto = new ResponseEntity<>(bifurcateConsumptionDto, HttpStatus.OK);
            logger.info(methodName + "return. bifurcateConsumptionDto: {} ",bifurcateConsumptionDto.toString());
        } catch (ApiException apiException) {
            alreadyBifurcatedBeanDto = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch (DataIntegrityViolationException d) {
            alreadyBifurcatedBeanDto = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}", d.getMessage());
        } catch (Exception e) {
            alreadyBifurcatedBeanDto = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
        }
        return alreadyBifurcatedBeanDto;
    }
}
