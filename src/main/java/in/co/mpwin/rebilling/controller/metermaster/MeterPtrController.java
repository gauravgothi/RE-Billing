package in.co.mpwin.rebilling.controller.metermaster;


import in.co.mpwin.rebilling.beans.metermaster.MeterMasterBean;
import in.co.mpwin.rebilling.beans.metermaster.MeterPtr;
import in.co.mpwin.rebilling.services.metermaster.MeterPtrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meter/ptr")
@CrossOrigin(origins="*")
public class MeterPtrController
{
    @Autowired
    MeterPtrService meterPtrService;


    @RequestMapping(method= RequestMethod.GET,value="/list")
    public ResponseEntity<List<MeterPtr>> getMeterPtrDetails()
    {  String status = "active";
        ResponseEntity meterPtrResp = null;
        try{
            List<MeterPtr> meterPtr = meterPtrService.getMeterPtrDetails(status);
            if(meterPtr.size()>0)
            {
                meterPtrResp = new ResponseEntity<>(meterPtr, HttpStatus.OK);
            }else if(meterPtr.size()==0)
            {
                meterPtrResp =new ResponseEntity<>("Meter Ptr Details not present",HttpStatus.NO_CONTENT);
            } else
            {
                meterPtrResp=new ResponseEntity<>("Invalid Request",HttpStatus.BAD_REQUEST);
            }
            } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return meterPtrResp;

        }

    @RequestMapping(method= RequestMethod.GET,value="/id/{id}")
    public ResponseEntity<MeterPtr> getMeterPtrDetails(@PathVariable("id") Long id)
    {
        ResponseEntity meterPtrResp = null;
        try
        {
        MeterPtr meterPtr =new MeterPtr();
        meterPtr =  meterPtrService.getMeterPtrDetails(id);
            if(meterPtr!=null)
            {
                meterPtrResp = new ResponseEntity<>(meterPtr, HttpStatus.OK);
            }
            else if(meterPtr==null)
            {
                meterPtrResp=new ResponseEntity<>("Meter Detail not present",HttpStatus.NO_CONTENT);
            }
            else {
                meterPtrResp=new ResponseEntity<>("Invalid Request",HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return meterPtrResp;

        }

}





