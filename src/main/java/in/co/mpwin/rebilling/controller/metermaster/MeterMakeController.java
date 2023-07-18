package in.co.mpwin.rebilling.controller.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterCtr;
import in.co.mpwin.rebilling.beans.metermaster.MeterMake;
import in.co.mpwin.rebilling.services.metermaster.MeterCtrService;
import in.co.mpwin.rebilling.services.metermaster.MeterMakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/meter/make")
@CrossOrigin(origins="*")
public class MeterMakeController {

    @Autowired
    MeterMakeService meterMakeService;

    @RequestMapping(method= RequestMethod.GET,value="/list")
    public ResponseEntity<MeterMake> getAllMeterMakeByStatus()    {
        ResponseEntity meterMakeResp = null;
        try {
            String status = "active";
            List<MeterMake> meterMakeList = meterMakeService.findAllByStatus(status);

            if(meterMakeList.size()>0)
            {
                meterMakeResp = new ResponseEntity<>(meterMakeList, HttpStatus.OK);
            }
            else if(meterMakeList.size()==0)
            {
                meterMakeResp=new ResponseEntity<>("Meter Make details is not available",HttpStatus.NO_CONTENT);
            }
        }catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return meterMakeResp;
    }
}
