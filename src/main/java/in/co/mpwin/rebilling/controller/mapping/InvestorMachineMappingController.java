package in.co.mpwin.rebilling.controller.mapping;

import in.co.mpwin.rebilling.beans.mapping.InvestorMachineMappingBean;
import in.co.mpwin.rebilling.dto.InvestorMachineMappingDto;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.mapping.InvestorMachineMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/im/mapping")
@CrossOrigin(origins="*")
public class InvestorMachineMappingController {

    @Autowired private InvestorMachineMappingService investorMachineMappingService;

    @RequestMapping(method = RequestMethod.POST,value = "")
    public ResponseEntity<?> createInvestorMachineMapping(@RequestBody InvestorMachineMappingDto investorMachineMappingDto){
        ResponseEntity resp = null;

        try {

          String msg  = investorMachineMappingService.createMapping(investorMachineMappingDto);
          resp =  new ResponseEntity<>(new Message(msg), HttpStatus.CREATED);

        }catch (ApiException apiException) {
            resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException d) {

            resp = new ResponseEntity<>(new Message("Data Integrity Violation :"+d.getMessage().substring(0,d.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }

        return resp;
    }
    @RequestMapping(method= RequestMethod.GET,value="")
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

    @RequestMapping(method = RequestMethod.GET,value = "/id/{id}")
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


    @RequestMapping(method=RequestMethod.GET, value="/mfpId/{mfpId}")
    public ResponseEntity<?> getMappingMFPId(@PathVariable("mfpId") Long mfpId){
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


    @RequestMapping(method=RequestMethod.GET,value="/investorCode/{investorCode}")
    public ResponseEntity<?> getMappingByInvestorCode(@PathVariable("investorCode") String investorCode ){
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
    @RequestMapping(method=RequestMethod.GET,value="/machineCode/{machineCode}")
    public ResponseEntity<?> getMappingByMachineCode(@PathVariable("machineCode") String machineCode ){
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
