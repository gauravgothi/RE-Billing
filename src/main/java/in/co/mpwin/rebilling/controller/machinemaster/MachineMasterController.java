package in.co.mpwin.rebilling.controller.machinemaster;


import in.co.mpwin.rebilling.beans.machinemaster.MachineMasterBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.machinemaster.MachineMasterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/machine")
@CrossOrigin(origins="*")
public class MachineMasterController {

    @Autowired
    MachineMasterService machineMasterService;

    @RequestMapping(method= RequestMethod.GET,value="")
    public ResponseEntity<MachineMasterBean> getAllMachineMaster(){
        ResponseEntity machineResp = null;
        try {
            String status = "active";
            List<MachineMasterBean> machineList = machineMasterService.getAllMachineMasterBean(status);

            if(machineList.size()>0)
            {
                machineResp= new ResponseEntity<>(machineList, HttpStatus.OK);
            }
            else if(machineList.size()==0)
            {
                machineResp=new ResponseEntity<>( new Message("Machine list is not available"),HttpStatus.BAD_REQUEST);
            }
        }catch (ApiException apiException) {
            machineResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {
            machineResp = new ResponseEntity<>(new Message("Data Integrity Violation"+d.getMessage().substring(0, 200)),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            machineResp = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, 200)),HttpStatus.BAD_REQUEST);
        }
        return machineResp;
    }


    @RequestMapping(method = RequestMethod.POST,value = "")
    public ResponseEntity<?> createMachineMaster(@Valid @RequestBody MachineMasterBean machineMasterBean){
        MachineMasterBean mmb = new MachineMasterBean();
        ResponseEntity machineInsrtResp = null;
        try {

            mmb = machineMasterService.createMachineMaster(machineMasterBean);

            if(mmb!=null)
            {
                machineInsrtResp =  new ResponseEntity<>(new Message(mmb.getMachineCode() + " is created successfully."),HttpStatus.OK);
            }else if(mmb==null) {
                machineInsrtResp = new ResponseEntity<>(new Message(machineMasterBean.getMachineCode() + " is already exist."), HttpStatus.BAD_REQUEST);
            }else {
                machineInsrtResp = new ResponseEntity<>(new Message("something went wrong"), HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return machineInsrtResp;
    }


    @RequestMapping(method = RequestMethod.GET,value = "/machineCode/{machineCode}")
    public ResponseEntity<?> getMachineByMachineCode(@PathVariable("machineCode") String machineCode){
        String status = "active";
        ResponseEntity machineResp = null;
        MachineMasterBean machineBean = null;
        try {
            machineBean = machineMasterService.getMachineByMachineCode(machineCode,status);
            if (machineBean!= null) {
                machineResp  = new ResponseEntity<>(machineBean, HttpStatus.OK);
            } else if (machineBean == null) {
                machineResp  = new ResponseEntity<>(new Message(machineCode + " does not exist."), HttpStatus.BAD_REQUEST);
            } else {
                machineResp = new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return machineResp ;
    }
    @RequestMapping(method = RequestMethod.GET,value = "/id/{id}")
    public ResponseEntity<?> getMachineById(@PathVariable("id") Long id){
        String status = "active";
        ResponseEntity machineResp = null;
        MachineMasterBean machineBean = null;
        try {
            machineBean = machineMasterService.getMachineById(id, status);
            if (machineBean  != null) {
                machineResp = new ResponseEntity<>(machineBean , HttpStatus.OK);
            } else if (machineBean  == null) {
                machineResp = new ResponseEntity<>(new Message(id + " id does not exist."), HttpStatus.BAD_REQUEST);
            } else {
                machineResp = new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return machineResp;
    }


    @RequestMapping(method = RequestMethod.GET,value = "/locationId/{locationId}")
    public ResponseEntity<?> getMachineByLocationId(@PathVariable("locationId") String locationId){
        String status = "active";
        ResponseEntity machineResp = null;
        try {
            List<MachineMasterBean> machineList = machineMasterService.getAllMachineByLocationId(locationId, status);
            if (machineList.size()>0) {
                machineResp = new ResponseEntity<>(machineList, HttpStatus.OK);
            } else if (machineList.size() == 0) {
                machineResp = new ResponseEntity<>(new Message( " machine list does not exist for "+locationId), HttpStatus.BAD_REQUEST);
            } else {
                machineResp = new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return machineResp;
    }

}
