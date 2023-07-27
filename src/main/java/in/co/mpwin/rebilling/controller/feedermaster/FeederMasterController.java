package in.co.mpwin.rebilling.controller.feedermaster;

import in.co.mpwin.rebilling.beans.feedermaster.FeederMasterBean;
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

}
