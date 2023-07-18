package in.co.mpwin.rebilling.controller.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterDmf;
import in.co.mpwin.rebilling.beans.metermaster.MeterMake;
import in.co.mpwin.rebilling.services.metermaster.MeterDmfService;
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
@RequestMapping("/meter/dmf")
@CrossOrigin(origins="*")
public class MeterDmfController {

    @Autowired
    MeterDmfService meterDmfService;

    @RequestMapping(method= RequestMethod.GET,value="/list")
    public ResponseEntity<MeterDmf> getAllMeterDmf()    {
        ResponseEntity meterDmfResp = null;
        try {
            List<MeterDmf> meterDmfList = meterDmfService.findAll();

            if(meterDmfList.size()>0)
            {
                meterDmfResp = new ResponseEntity<>(meterDmfList, HttpStatus.OK);
            }
            else if(meterDmfList.size()==0)
            {
                meterDmfResp=new ResponseEntity<>("Meter DMF details is absent",HttpStatus.NO_CONTENT);
            }
        }catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return meterDmfResp;
    }
}
