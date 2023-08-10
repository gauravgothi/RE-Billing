package in.co.mpwin.rebilling.controller.readingcontroller;

import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.readingservice.MeterReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meter_reading")
@CrossOrigin(origins="*")
public class MeterReadingController {
    @Autowired
    MeterReadingService meterReadingService;

    @RequestMapping(method= RequestMethod.GET,value="/status/{status}")
    public ResponseEntity<?> getAllMeterReadingByStatus(@PathVariable("status") String status){
        ResponseEntity meterReadingResp = null;
        try {
            List<MeterReadingBean> meterReadingBeanList = meterReadingService.getAllMeterReading(status);

            if (!meterReadingBeanList.isEmpty())
                meterReadingResp = new ResponseEntity<>(meterReadingBeanList, HttpStatus.OK);
            else if (meterReadingBeanList.isEmpty())
                meterReadingResp = new ResponseEntity<>(new Message("Meter Reading list is not available."),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return meterReadingResp;
    }
}
