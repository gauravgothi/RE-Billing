package in.co.mpwin.rebilling.controller.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterCtr;
import in.co.mpwin.rebilling.beans.metermaster.MeterMeCtr;
import in.co.mpwin.rebilling.services.metermaster.MeterCtrService;
import in.co.mpwin.rebilling.services.metermaster.MeterMeCtrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/meter/me-ctr")
@CrossOrigin(origins="*")
public class MeterMeCtrController {

    @Autowired
    MeterMeCtrService meterMeCtrService;

    @RequestMapping(method= RequestMethod.GET,value="/list")
    public ResponseEntity<MeterMeCtr> getAllMeterMeCtr()    {
        ResponseEntity meterMeCtrResp = null;
        try {
            List<MeterMeCtr> meterMeCtrList = meterMeCtrService.findAll();

            if(meterMeCtrList.size()>0)
            {
                meterMeCtrResp = new ResponseEntity<>(meterMeCtrList, HttpStatus.OK);
            }
            else if(meterMeCtrList.size()==0)
            {
                meterMeCtrResp=new ResponseEntity<>("Meter Me Ctr details is absent",HttpStatus.NO_CONTENT);
            }
        }catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return meterMeCtrResp;
    }
}
