package in.co.mpwin.rebilling.controller.metermaster;


import in.co.mpwin.rebilling.beans.metermaster.MeterMePtr;
import in.co.mpwin.rebilling.beans.metermaster.MeterPtr;
import in.co.mpwin.rebilling.services.metermaster.MeterMePtrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/meter/me-ptr")
@CrossOrigin(origins="*")
public class MeterMePtrController {

    @Autowired
    MeterMePtrService meterMePtrService;


    @RequestMapping(method= RequestMethod.GET,value="")
    public ResponseEntity<MeterMePtr> getAllMeterMePtrByStatus()
    {
        ResponseEntity meterMePtrResp = null;
        try{
            String status = "active";
            List<MeterMePtr> meterMePtrList = meterMePtrService.findAllByStatus(status);
            if(meterMePtrList.size()>0)
            {
                meterMePtrResp = new ResponseEntity<>(meterMePtrList, HttpStatus.OK);
            }else if(meterMePtrList.size()==0)
            {
                meterMePtrResp =new ResponseEntity<>("Meter Ptr Details not present",HttpStatus.NO_CONTENT);
            } else
            {
                meterMePtrResp=new ResponseEntity<>("Invalid Request",HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return meterMePtrResp;

    }

}
