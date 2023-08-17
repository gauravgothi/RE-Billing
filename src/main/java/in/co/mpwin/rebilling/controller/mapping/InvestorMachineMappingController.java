package in.co.mpwin.rebilling.controller.mapping;

import in.co.mpwin.rebilling.beans.mapping.InvestorMachineMappingBean;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.mapping.InvestorMachineMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mapping")
@CrossOrigin(origins="*")
public class InvestorMachineMappingController {

    @Autowired
    InvestorMachineMappingService investorMachineMappingService;

    @RequestMapping(method = RequestMethod.POST,value = "/IM_Mapping")
    public ResponseEntity<?> createInvestorMachineMapping(@RequestBody InvestorMachineMappingBean investorMachineMappingBean){
        ResponseEntity resp = null;
        InvestorMachineMappingBean imm = new InvestorMachineMappingBean();
        try {

            imm  = investorMachineMappingService.createMapping(investorMachineMappingBean);

            if(imm!=null)
            {
                resp =  new ResponseEntity<>(new Message(imm.getId() + " mapping is created successfully."), HttpStatus.OK);
            }else if(imm==null) {
                resp = new ResponseEntity<>(new Message( "Mapping is already exist."), HttpStatus.BAD_REQUEST);
            }else {
                resp = new ResponseEntity<>(new Message("something went wrong"), HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }

        return resp;
    }
    @RequestMapping(method= RequestMethod.GET,value="/IM_Mapping")
    public ResponseEntity<InvestorMachineMappingBean> getAllMapping(){
        ResponseEntity resp = null;
        try {
            String status = "active";
            List<InvestorMachineMappingBean> mappingList = investorMachineMappingService.getAllMapping(status);

            if(mappingList.size()>0)
            {
                resp = new ResponseEntity<>(mappingList, HttpStatus.OK);
            }
            else if(mappingList.size()==0)
            {
                resp =new ResponseEntity<>(new Message("mapping is not available"),HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return resp;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/IM_Mapping/id/{id}")
    public ResponseEntity<?> getMappingId(@PathVariable("id") Long id){
        String status = "active";
        ResponseEntity resp = null;
        InvestorMachineMappingBean mappingBean = null;
        try {
            mappingBean = investorMachineMappingService.getMappingById(id,status);
            if ( mappingBean!=null) {
                resp = new ResponseEntity<>( mappingBean, HttpStatus.OK);
            } else if ( mappingBean==null) {
                resp = new ResponseEntity<>(new Message(id + " id does not exist."), HttpStatus.BAD_REQUEST);
            } else {
                resp = new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return resp;
    }


    @RequestMapping(method=RequestMethod.GET, value="/IM_Mapping/mfp_id/{mfp_id}")
    public ResponseEntity<?> getMappingMFPId(@PathVariable("mfp_id") Long mfpId){
        String status = "active";
        ResponseEntity resp = null;
        List<InvestorMachineMappingBean> mappingBean = null;
        try {
            mappingBean = investorMachineMappingService.getMappingByMFPId(mfpId,status);
            if (mappingBean.size()>0) {
                resp = new ResponseEntity<>( mappingBean, HttpStatus.OK);
            } else if ( mappingBean.size()==0) {
                resp = new ResponseEntity<>(new Message(mfpId + " id does not exist."), HttpStatus.BAD_REQUEST);
            } else {
                resp = new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return resp;
    }


    @RequestMapping(method=RequestMethod.GET,value="/IM_Mapping/investor_code/{investor_code}")
    public ResponseEntity<?> getMappingByInvestorCode(@PathVariable("investor_code") String investorCode ){
        String status = "active";
        ResponseEntity resp = null;
        List<InvestorMachineMappingBean> mappingBean = null;
        try {
            mappingBean = investorMachineMappingService.getMappingByInvestorCode(investorCode, status);
            if (mappingBean.size()>0) {
                resp = new ResponseEntity<>( mappingBean, HttpStatus.OK);
            } else if (mappingBean.size()==0) {
                resp = new ResponseEntity<>(new Message(investorCode+ " is not exist."), HttpStatus.BAD_REQUEST);
            } else {
                resp = new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return resp;
    }
    @RequestMapping(method=RequestMethod.GET,value="/IM_Mapping/machine_code/{machine_code}")
    public ResponseEntity<?> getMappingByMachineCode(@PathVariable("machine_code") String machineCode ){
        String status = "active";
        ResponseEntity resp = null;
        List<InvestorMachineMappingBean> mappingBean = null;
        try {
            mappingBean = investorMachineMappingService.getMappingByMachineCode(machineCode,status);
            if (mappingBean.size()>0) {
                resp = new ResponseEntity<>( mappingBean, HttpStatus.OK);
            } else if (mappingBean.size()==0) {
                resp = new ResponseEntity<>(new Message(machineCode+ " is not exist."), HttpStatus.BAD_REQUEST);
            } else {
                resp = new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return resp;
    }


}
