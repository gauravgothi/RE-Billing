package in.co.mpwin.rebilling.controller.metermaster;

import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.dto.MeterReplacementDto;
import in.co.mpwin.rebilling.dto.MeterReplacementRequest;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.services.metermaster.MeterMasterService;
import in.co.mpwin.rebilling.services.metermaster.MeterReplacementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meter/replace")
@CrossOrigin(origins="*")
public class MeterReplacementController {
    @Autowired
    MeterReplacementService meterReplacementService;

    @RequestMapping(method = RequestMethod.POST, value = "")
    public ResponseEntity<?> meterReplacement(@RequestBody MeterReplacementDto meterReplacementDto) {
        try {
            String res = meterReplacementService.replaceMeter(meterReplacementDto);

            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (ApiException ex) {
            return ResponseEntity.status(ex.getHttpStatus()).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/m2")
    public ResponseEntity<?> meterReplacement2(@RequestBody MeterReplacementRequest meterReplacementRequest) {
        try {
            String res = meterReplacementService.replaceMeter2(meterReplacementRequest.getOldMeterBean(), meterReplacementRequest.getNewMeterBean());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getHttpStatus()).body(apiException.getMessage());
        } catch (DataIntegrityViolationException d) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(d.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }
    }

}
