package in.co.mpwin.rebilling.controller.metermaster;

import in.co.mpwin.rebilling.dto.MeterReplacementDto;
import in.co.mpwin.rebilling.services.metermaster.MeterMasterService;
import in.co.mpwin.rebilling.services.metermaster.MeterReplacementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meter/replace")
@CrossOrigin(origins="*")
public class MeterReplacementController {
    @Autowired
    MeterReplacementService meterReplacementService;

    @RequestMapping(method= RequestMethod.POST,value="")
    public ResponseEntity<?> meterReplacement(@RequestBody MeterReplacementDto meterReplacementDto)
    {
        String res = meterReplacementService.replaceMeter(meterReplacementDto);

        return ResponseEntity.status(HttpStatus.OK).body(res);

    }


}
