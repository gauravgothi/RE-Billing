package in.co.mpwin.rebilling.controller.developermaster;

import in.co.mpwin.rebilling.beans.developermaster.DeveloperMasterBean;
import in.co.mpwin.rebilling.beans.feedermaster.FeederMasterBean;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.developermaster.DeveloperMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/developer")
@CrossOrigin(origins="*")
public class DeveloperMasterController {
    @Autowired
    DeveloperMasterService developerMasterService;

    @RequestMapping(method= RequestMethod.GET,value="")
    public ResponseEntity<?> getAllDeveloperMaster(){
        ResponseEntity developerResp = null;
        try {
            String status = "active";
            List<DeveloperMasterBean> developerMasterBeanList = developerMasterService.getAllDeveloperMasterBean(status);

            if (!developerMasterBeanList.isEmpty())
                developerResp = new ResponseEntity<>(developerMasterBeanList, HttpStatus.OK);
            else if (developerMasterBeanList.isEmpty())
                developerResp = new ResponseEntity<>(new Message("Developer list is not available."),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return developerResp;
    }

    @RequestMapping(method= RequestMethod.GET,value="/locationId/{locationId}")
    public ResponseEntity<?> getAllDeveloperByLocationId(@PathVariable("locationId") String locationId){
        ResponseEntity developerResp = null;
        try {
            String status = "active";
            List<DeveloperMasterBean> developerMasterBeanList = developerMasterService.getAllDeveloperByLocationId(locationId,status);
            if (!developerMasterBeanList.isEmpty())
                developerResp = new ResponseEntity<>(developerMasterBeanList, HttpStatus.OK);
            else if (developerMasterBeanList.isEmpty())
                developerResp = new ResponseEntity<>(new Message("Developer list is not available."),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return developerResp;
    }

    @RequestMapping(method = RequestMethod.POST,value = "")
    public ResponseEntity<?> createDeveloperMaster(@Valid @RequestBody DeveloperMasterBean developerMasterBean){
        DeveloperMasterBean dmb = new DeveloperMasterBean();
        ResponseEntity developerInsrtResp = null;
        try {

            dmb = developerMasterService.createDeveloperMaster(developerMasterBean);

            if(dmb!=null)
            {
                //meterInsrtResp = new ResponseEntity<>(meterMasterBean.getMeterNumber()+" is created successfully", HttpStatus.OK);
                developerInsrtResp =  new ResponseEntity<>(new Message(dmb.getDeveloperName() + " is created successfully."),HttpStatus.OK);
            }else if(dmb==null) {
                developerInsrtResp = new ResponseEntity<>(new Message("something went wrong"), HttpStatus.BAD_REQUEST);
            }else {
                developerInsrtResp = new ResponseEntity<>(new Message("something went wrong"), HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return developerInsrtResp;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/id/{id}")
    public ResponseEntity<?> getDeveloperById(@PathVariable("id") Long id){
        String status = "active";
        ResponseEntity developerResp = null;
        try {
            DeveloperMasterBean developer = developerMasterService.getDeveloperById(id,status);
            if (developer != null) {
                developerResp = new ResponseEntity<>(developer, HttpStatus.OK);
            } else if (developer == null) {
                developerResp = new ResponseEntity<>(new Message(id + " id does not exist."), HttpStatus.BAD_REQUEST);
            } else {
                developerResp = new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return developerResp;
        }

}
