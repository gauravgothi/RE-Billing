package in.co.mpwin.rebilling.controller.thirdparty;

import in.co.mpwin.rebilling.beans.investormaster.InvestorMasterBean;
import in.co.mpwin.rebilling.beans.invoice.InvoiceBean;
import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import in.co.mpwin.rebilling.beans.thirdparty.DeveloperPlantDto;
import in.co.mpwin.rebilling.beans.thirdparty.ThirdPartyBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.mapping.MeterFeederPlantMappingService;
import in.co.mpwin.rebilling.services.thirdparty.ThirdPartyService;
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

    @Autowired private ThirdPartyService thirdPartyService;

    @Autowired private MeterFeederPlantMappingService meterFeederPlantMappingService;

    // save third party bean into database
    @PostMapping("/save")
    public ResponseEntity<?> saveThirdPartyBean(@RequestBody ThirdPartyBean tpBean) {
        ResponseEntity resp = null;
        try {
            ThirdPartyBean saveBean = thirdPartyService.saveThirdPartyBean(tpBean);
            resp = new ResponseEntity<>(saveBean, HttpStatus.OK);
        } catch (ApiException apiException) {
            resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {

            resp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, 200)), HttpStatus.BAD_REQUEST);
        }
        return resp;
    }

    // get active third party list

    @GetMapping("/list")
    public ResponseEntity<?> getThirdPartyBeans() {
        ResponseEntity resp = null;
        try {
            List<ThirdPartyBean> tpLists = thirdPartyService.getThirdPartyBeans();
            resp = new ResponseEntity<>(tpLists, HttpStatus.OK);
        } catch (ApiException apiException) {
            resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            resp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, 200)), HttpStatus.BAD_REQUEST);
        }
        return resp;
    }

    @GetMapping("/list/status/{status}")
    public ResponseEntity<?> getThirdPartyBeansByStatus(@PathVariable("status") String status) {
        ResponseEntity resp = null;
        try {
            List<ThirdPartyBean> tpLists = thirdPartyService.getThirdPartyBeansByStatus(status);
            resp = new ResponseEntity<>(tpLists, HttpStatus.OK);
        } catch (ApiException apiException) {
            resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            resp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, 200)), HttpStatus.BAD_REQUEST);
        }
        return resp;
    }
    @GetMapping("/dto/developerId/{developerId}/plantCode/{plantCode}")
    public ResponseEntity<?> getMfpMapping(@PathVariable("developerId") String developerId, @PathVariable("plantCode") String plantCode)
    {
        ResponseEntity resp = null;
        try {
            DeveloperPlantDto developerPlantDto = thirdPartyService.getDeveloperPlantDto(developerId,plantCode);
            resp = new ResponseEntity<>(developerPlantDto, HttpStatus.OK);
        } catch (ApiException apiException) {
            resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            resp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, 200)), HttpStatus.BAD_REQUEST);
        }
        return resp;
    }


    @GetMapping("/list/consumerCode/{consumerCode}")
    public ResponseEntity<?>  getThirdPartyBeansByConsumerCode(@PathVariable String consumerCode) {
        ResponseEntity resp = null;
        try {
            List<ThirdPartyBean> tpLists = thirdPartyService.getThirdPartyByConsumerCode(consumerCode,"active");
            resp = new ResponseEntity<>(tpLists, HttpStatus.OK);
        } catch (ApiException apiException) {
            resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            resp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, 200)), HttpStatus.BAD_REQUEST);
        }
        return resp;
    }

    @GetMapping("/list/investorCode/{investorCode}")
    public ResponseEntity<?>  getThirdPartyBeansByInvestorId(@PathVariable String investorCode) {
        ResponseEntity resp = null;
        try {
            List<ThirdPartyBean> tpLists = thirdPartyService.getThirdPartiesByInvestorId(investorCode,"active");
            resp = new ResponseEntity<>(tpLists, HttpStatus.OK);
        } catch (ApiException apiException) {
            resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            resp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, 200)), HttpStatus.BAD_REQUEST);
        }
        return resp;
    }

    @GetMapping("/list/plantCode/{plantCode}")
    public ResponseEntity<?>  getThirdPartyBeansByPlantCode(@PathVariable String plantCode) {
        ResponseEntity resp = null;
        try {
            List<ThirdPartyBean> tpLists = thirdPartyService.getThirdPartiesByPlantCode(plantCode,"active");
            resp = new ResponseEntity<>(tpLists, HttpStatus.OK);
        } catch (ApiException apiException) {
            resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            resp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, 200)), HttpStatus.BAD_REQUEST);
        }
        return resp;
    }

    @GetMapping("/investors/developerId/{developerId}/plantCode/{plantCode}")
    public ResponseEntity<?> getInvestors(@PathVariable("developerId") String developerId, @PathVariable("plantCode") String plantCode)
    {
        ResponseEntity resp = null;
        try {
            List<InvestorMasterBean> investors = thirdPartyService.getInvestorsByDeveloperIdAndPlantCode(developerId,plantCode);
            resp = new ResponseEntity<>(investors, HttpStatus.OK);
        } catch (ApiException apiException) {
            resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            resp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, 200)), HttpStatus.BAD_REQUEST);
        }
        return resp;
    }

    @PutMapping("/inactive")
    public ResponseEntity<?> setThirdPartyInactive(@RequestBody ThirdPartyBean thirdParty)
    {
        ResponseEntity resp = null;
        try {
            ThirdPartyBean tpBean = thirdPartyService.setThirdPartyInactive(thirdParty);
            resp = new ResponseEntity<>(tpBean,HttpStatus.OK);
        }catch (ApiException apiException) {
            resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            resp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, 200)), HttpStatus.BAD_REQUEST);
        }
        return resp;
    }

    @PutMapping("/active")
    public ResponseEntity<?> setThirdPartyActive(@RequestBody ThirdPartyBean thirdParty)
    {
        ResponseEntity resp = null;
        try {
            ThirdPartyBean tpBean = thirdPartyService.setThirdPartyActive(thirdParty);
            resp = new ResponseEntity<>(tpBean,HttpStatus.OK);
        }catch (ApiException apiException) {
            resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            resp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, 200)), HttpStatus.BAD_REQUEST);
        }
        return resp;
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateThirdPartyInactive(@RequestBody ThirdPartyBean thirdParty)
    {
        ResponseEntity resp = null;
        try {
            ThirdPartyBean tpBean = thirdPartyService.updateThirdParty(thirdParty);
            resp = new ResponseEntity<>(tpBean,HttpStatus.OK);
        }catch (ApiException apiException) {
            resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            resp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, 200)), HttpStatus.BAD_REQUEST);
        }
        return resp;
    }
}
