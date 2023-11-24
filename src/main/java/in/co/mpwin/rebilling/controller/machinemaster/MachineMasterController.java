package in.co.mpwin.rebilling.controller.machinemaster;


import in.co.mpwin.rebilling.beans.machinemaster.MachineMasterBean;
import in.co.mpwin.rebilling.controller.feedermaster.FeederMasterController;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.machinemaster.MachineMasterService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(MachineMasterController.class);
    @Autowired
    MachineMasterService machineMasterService;

    @RequestMapping(method= RequestMethod.GET,value="")
    public ResponseEntity<MachineMasterBean> getAllMachineMaster(){
        final String methodName = "getAllMachineMaster() : ";
        logger.info(methodName + "called with parameters empty");
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
            logger.info(methodName + "return with MachineMasterBean list of size : {} ",machineList.size());
            }catch(ApiException apiException) {
                resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch(DataIntegrityViolationException e) {
                resp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
            } catch(Exception e) {
                resp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
            }
        return resp;
    }


    @RequestMapping(method = RequestMethod.POST,value = "")
    public ResponseEntity<?> createMachineMaster(@Valid @RequestBody MachineMasterBean machineMasterBean){
        final String methodName = "createMachineMaster() : ";
        logger.info(methodName + "called with parameter machineMasterBean={}",machineMasterBean);
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
            logger.info(methodName + "return with MachineMasterBean : {} ",mmb);
            }catch(ApiException apiException) {
                resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch(DataIntegrityViolationException e) {
                resp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
            } catch(Exception e) {
                resp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
            }
        return resp;
    }


    @RequestMapping(method = RequestMethod.GET,value = "/machineCode/{machineCode}")
    public ResponseEntity<?> getMachineByMachineCode(@PathVariable("machineCode") String machineCode){
        final String methodName = "getMachineByMachineCode() : ";
        logger.info(methodName + "called with parameter machineCode={}",machineCode);
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
            logger.info(methodName + "return with machineBean : {} ",machineBean);
            }catch(ApiException apiException) {
            machineResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch(DataIntegrityViolationException e) {
            machineResp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
            } catch(Exception e) {
            machineResp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
            }
        return machineResp ;
    }
    @RequestMapping(method = RequestMethod.GET,value = "/id/{id}")
    public ResponseEntity<?> getMachineById(@PathVariable("id") Long id){
        final String methodName = "getMachineById() : ";
        logger.info(methodName + "called with parameter id={}",id);
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
            logger.info(methodName + "return with machineBean : {} ",machineBean);
            }catch(ApiException apiException) {
                machineResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch(DataIntegrityViolationException e) {
                machineResp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
            } catch(Exception e) {
                machineResp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
            }
        return machineResp;
    }


    @RequestMapping(method = RequestMethod.GET,value = "/locationId/{locationId}")
    public ResponseEntity<?> getMachineByLocationId(@PathVariable("locationId") String locationId){
        final String methodName = "getMachineByLocationId() : ";
        logger.info(methodName + "called with parameter locationId={}",locationId);
        String status = "active";
        ResponseEntity machineResp = null;
        try {
            List<MachineMasterBean> machineList = machineMasterService.getAllMachineByLocationId(locationId, status);
            if (machineList.size()>0) {
                machineResp = new ResponseEntity<>(machineList, HttpStatus.OK);
            } else if (machineList.size() == 0) {
                machineResp = new ResponseEntity<>(new Message( " machine list does not exist for "+locationId), HttpStatus.BAD_REQUEST);
            }
            logger.info(methodName + "return with machineBean list of size  : {} ",machineList.size());

        } catch(ApiException apiException) {
            machineResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch(DataIntegrityViolationException e) {
            machineResp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
        } catch(Exception e) {
            machineResp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
        }
        return machineResp;
    }


    // get list of machine from machine master excluding machine code which is present in investor_machine mapping table
    @RequestMapping(method = RequestMethod.GET,value = "/list/unmapped")
    public ResponseEntity<?> getUnmappedMachineList() {
        final String methodName = "getUnmappedMachineList() : ";
        logger.info(methodName + "called with parameter empty");
        ResponseEntity resp = null;
        try {
            List<MachineMasterBean> unmappedMachines = machineMasterService.getUnmappedMachineBeans();
            resp = new ResponseEntity<>(unmappedMachines,HttpStatus.OK);
            logger.info(methodName + "return with unmapped machineBean list of size  : {} ",unmappedMachines.size());
            }catch(ApiException apiException) {
            resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            }catch(DataIntegrityViolationException e) {
            resp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
            }catch(Exception e) {
            resp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
            }
        return resp;
    }


}
