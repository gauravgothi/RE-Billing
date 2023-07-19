package in.co.mpwin.rebilling.controller.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterCtr;
import in.co.mpwin.rebilling.interfaces.BeanInterface;
import in.co.mpwin.rebilling.services.metermaster.MeterCtrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meter/ctr")
@CrossOrigin(origins="*")
public class MeterCtrController {

    @Autowired
    MeterCtrService meterCtrService;

    @RequestMapping(method= RequestMethod.GET,value="")
    public ResponseEntity<MeterCtr> getAllMeterCtrByStatus()    {
        ResponseEntity meterCtrResp = null;
        try {
            String status = "active";
            List<MeterCtr> meterCtrList = meterCtrService.findAllByStatus(status);

            if(meterCtrList.size()>0)
            {
                meterCtrResp = new ResponseEntity<>(meterCtrList, HttpStatus.OK);
            }
            else if(meterCtrList.size()==0)
            {
                meterCtrResp=new ResponseEntity<>("Meter Ctr details is not available",HttpStatus.NO_CONTENT);
            }
        }catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return meterCtrResp;
    }
//    @RequestMapping(method= RequestMethod.GET,value="/capacity/{capacity}")
//    public ResponseEntity<?> getMeterCtrValue(@PathVariable("capacity") String capacity){
//        ResponseEntity meterCtrValueResp = null;
//        try {
//                String meterCtrValue="";
//                MeterCtr meterCtr = meterCtrService.findByCapacity(capacity);
//                meterCtrValue = meterCtr.getValue();
//
//            if(!meterCtrValue.isEmpty())
//            {
//                meterCtrValueResp = new ResponseEntity<>(meterCtrValue, HttpStatus.OK);
//            }
//            else if (meterCtrValue.isEmpty())
//            {
//                meterCtrValueResp=new ResponseEntity<>("Meter Ctr Value is absent",HttpStatus.NO_CONTENT);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return meterCtrValueResp;
//    }
}
