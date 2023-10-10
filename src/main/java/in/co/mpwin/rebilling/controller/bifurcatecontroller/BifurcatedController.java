package in.co.mpwin.rebilling.controller.bifurcatecontroller;

import in.co.mpwin.rebilling.beans.bifurcation.BifurcateBean;
import in.co.mpwin.rebilling.dto.BifurcateConsumptionDto;
import in.co.mpwin.rebilling.dto.MeterConsumptionDto;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.bifurcateservice.BifurcateConsumptionService;
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
    @Autowired
    private BifurcateConsumptionService bifurcateService;

    //This is used by developer to bifurcate the meter consumption
    @PostMapping("/empty")
    public ResponseEntity<?> getConsumptionBifurcateDto(@RequestBody MeterConsumptionDto dto) {
        ResponseEntity bifurcateResp = null;
        try {
            BifurcateConsumptionDto bifurcateConsumptionDto = bifurcateService.getBifurcateDto(dto);
            bifurcateResp = new ResponseEntity<>(bifurcateConsumptionDto, HttpStatus.OK);
        } catch (ApiException apiException) {
            bifurcateResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bifurcateResp;
    }

    @PostMapping("")
    public ResponseEntity<?> saveConsumptionBifurcateDto(@RequestBody BifurcateConsumptionDto dto) {
        ResponseEntity saveBifurcateResp = null;
        try {
            BifurcateConsumptionDto bifurcatedBean = bifurcateService.saveBifurcateDto(dto);
            saveBifurcateResp = new ResponseEntity<>(bifurcatedBean, HttpStatus.OK);
        } catch (ApiException apiException) {
            saveBifurcateResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            saveBifurcateResp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return saveBifurcateResp;
    }

    //get investor list associated with developer only on invoice generation page
    @GetMapping("/investor-list-lov")
    public ResponseEntity<?> getInvestorsByDeveloper() {
        ResponseEntity investorListResp = null;
        try {
            List<Map<String,String>> investorList = bifurcateService.getInvestorListByDeveloperId();
            investorListResp = new ResponseEntity<>(investorList, HttpStatus.OK);
        } catch (ApiException apiException) {
            investorListResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            investorListResp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return investorListResp;
    }

    //get meter list associated with developer only on invoice generation page
    @GetMapping("/meter-list-lov-for-developer")
    public ResponseEntity<?> getMetersByDeveloper() {
        ResponseEntity meterListResp = null;
        try {
            List<Map<String,String>> meterList = bifurcateService.getMeterListByDeveloperId();
            meterListResp = new ResponseEntity<>(meterList, HttpStatus.OK);
        } catch (ApiException apiException) {
            meterListResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            meterListResp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return meterListResp;
    }

    //get meter list associated with circle only on invoice generation page from bifurcated table
    @GetMapping("/meter-list-lov-for-circle")
    public ResponseEntity<?> getMetersByCircle() {
        ResponseEntity meterListResp = null;
        try {
            List<Map<String,String>> meterList = bifurcateService.getMeterListByCircleName();
            meterListResp = new ResponseEntity<>(meterList, HttpStatus.OK);
        } catch (ApiException apiException) {
            meterListResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            meterListResp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return meterListResp;
    }
}
