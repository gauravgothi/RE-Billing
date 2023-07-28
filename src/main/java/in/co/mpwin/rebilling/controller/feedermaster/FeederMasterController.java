package in.co.mpwin.rebilling.controller.feedermaster;

import in.co.mpwin.rebilling.beans.LocationMaster;
import in.co.mpwin.rebilling.beans.feedermaster.FeederMasterBean;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.feedermaster.FeederMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feeder")
@CrossOrigin(origins="*")
public class FeederMasterController {

    @Autowired
    FeederMasterService feederMasterService;

    @RequestMapping(method= RequestMethod.GET,value="")
    public ResponseEntity<FeederMasterBean> getAllFeederMaster(){
        ResponseEntity feederResp = null;
        try {
            String status = "active";
            List<FeederMasterBean> feederList = feederMasterService.getAllFeederMasterBean(status);

            if(feederList.size()>0)
            {
                feederResp = new ResponseEntity<>(feederList, HttpStatus.OK);
            }
            else if(feederList.size()==0)
            {
                feederResp=new ResponseEntity<>("Feeder list is not available",HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return feederResp;
    }

    @RequestMapping(method = RequestMethod.POST,value = "")
    public ResponseEntity<?> createFeederMaster(@RequestBody FeederMasterBean feederMasterBean){
        FeederMasterBean fmb = new FeederMasterBean();
        ResponseEntity feederInsrtResp = null;
        try {

            fmb = feederMasterService.createFeederMaster(feederMasterBean);

            if(fmb!=null)
            {
                //meterInsrtResp = new ResponseEntity<>(meterMasterBean.getMeterNumber()+" is created successfully", HttpStatus.OK);
                feederInsrtResp =  new ResponseEntity<>(new Message(fmb.getFeederNumber() + " is created successfully."),HttpStatus.OK);
            }else if(fmb==null) {
                feederInsrtResp = new ResponseEntity<>(new Message(feederMasterBean.getFeederNumber() + " is already exist."), HttpStatus.BAD_REQUEST);
            }else {
                feederInsrtResp = new ResponseEntity<>(new Message("something went wrong"), HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return feederInsrtResp;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/feederNumber/{feederNumber}")
    public ResponseEntity<?> getFeederByFeederNumber(@PathVariable("feederNumber") String feederNumber){
        String status = "active";
        ResponseEntity feederResp = null;
        FeederMasterBean feeder = null;
        try {
            feeder = feederMasterService.getFeederByFeederNumber(feederNumber, status);
            if (feeder != null) {
                feederResp = new ResponseEntity<>(feeder, HttpStatus.OK);
            } else if (feeder == null) {
                feederResp = new ResponseEntity<>(new Message(feederNumber + " number does not exist."), HttpStatus.BAD_REQUEST);
            } else {
                feederResp = new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return feederResp;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/id/{id}")
    public ResponseEntity<?> getFeederById(@PathVariable("id") Long id){
        String status = "active";
        ResponseEntity feederResp = null;
        FeederMasterBean feeder = null;
        try {
            feeder = feederMasterService.getFeederById(id, status);
            if (feeder != null) {
                feederResp = new ResponseEntity<>(feeder, HttpStatus.OK);
            } else if (feeder == null) {
                feederResp = new ResponseEntity<>(new Message(id + " id does not exist."), HttpStatus.BAD_REQUEST);
            } else {
                feederResp = new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return feederResp;
    }

}
