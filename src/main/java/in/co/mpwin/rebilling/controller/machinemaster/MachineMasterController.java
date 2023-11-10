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
        ResponseEntity resp = null;
        try {
            String status = "active";
            List<MachineMasterBean> machineList = machineMasterService.getAllMachineMasterBean(status);

            if(machineList.size()>0)
            {
                resp= new ResponseEntity<>(machineList, HttpStatus.OK);
            }
            else if(machineList.size()==0)
            {
                resp=new ResponseEntity<>( new Message("Machine list is not available"),HttpStatus.BAD_REQUEST);
            }
        }catch (ApiException apiException) {
            resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException e) {
            resp = new ResponseEntity<>(new Message("Data Integrity Violation : "+e.getMessage().substring(0, e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            resp = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return resp;
    }


    @RequestMapping(method = RequestMethod.POST,value = "")
    public ResponseEntity<?> createMachineMaster(@Valid @RequestBody MachineMasterBean machineMasterBean){
        MachineMasterBean mmb = new MachineMasterBean();
        ResponseEntity resp = null;
        try {

            mmb = machineMasterService.createMachineMaster(machineMasterBean);

            if(mmb!=null)
            {
                resp =  new ResponseEntity<>(new Message(mmb.getMachineCode() + " is created successfully."),HttpStatus.OK);
            }else if(mmb==null) {
                resp = new ResponseEntity<>(new Message("machine can not created due to some error."), HttpStatus.BAD_REQUEST);
            }
        }catch (ApiException apiException) {
            resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException e) {
            resp = new ResponseEntity<>(new Message("Data Integrity Violation : "+e.getMessage().substring(0, e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
           resp = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return resp;
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
            }
        } catch (ApiException apiException) {
            machineResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException e) {
            machineResp = new ResponseEntity<>(new Message("Data Integrity Violation : "+e.getMessage().substring(0, e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            machineResp = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
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
            }
            } catch (ApiException apiException) {
                machineResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            } catch (DataIntegrityViolationException e) {
                machineResp = new ResponseEntity<>(new Message("Data Integrity Violation : "+e.getMessage().substring(0, e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                machineResp = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
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
            }

        } catch (ApiException apiException) {
            machineResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException e) {
            machineResp = new ResponseEntity<>(new Message("Data Integrity Violation : "+e.getMessage().substring(0, e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            machineResp = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return machineResp;
    }


    // get list of machine from machine master excluding machine code which is present in investor_machine mapping table
    @RequestMapping(method = RequestMethod.GET,value = "/list/unmapped")
    public ResponseEntity<?> getUnmappedMachineList() {
        ResponseEntity resp = null;
        try {
            List<MachineMasterBean> unmappedMachines = machineMasterService.getUnmappedMachineBeans();
            resp = new ResponseEntity<>(unmappedMachines,HttpStatus.OK);
        } catch (ApiException apiException) {
            resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException e) {

            resp = new ResponseEntity<>(new Message("Data Integrity Violation : "+e.getMessage().substring(0, e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return resp;
    }


}
