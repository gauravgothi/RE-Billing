package in.co.mpwin.rebilling.controller.investormaster;

import in.co.mpwin.rebilling.beans.investormaster.InvestorMasterBean;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.investormaster.InvestorMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/investor")
@CrossOrigin(origins="*")
public class InvestorMasterController {

    @Autowired
    InvestorMasterService investorMasterService;

    @RequestMapping(method= RequestMethod.GET,value="")
    public ResponseEntity<?> getAllInvestorMasterBean(){
        ResponseEntity investorResp = null;
        try {
            String status = "active";
            List<InvestorMasterBean> investorMasterBeanList = investorMasterService.getAllInvestorMasterBean(status);

            if (!investorMasterBeanList.isEmpty())
                investorResp = new ResponseEntity<>(investorMasterBeanList, HttpStatus.OK);
            else if (investorMasterBeanList.isEmpty())
                investorResp = new ResponseEntity<>(new Message("Investor list is not available."),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return investorResp;
    }

    @RequestMapping(method= RequestMethod.GET,value="/locationId/{locationId}")
    public ResponseEntity<?> getAllInvestorByLocationId(@PathVariable("locationId") String locationId){
        ResponseEntity investorResp = null;
        try {
            String status = "active";
            List<InvestorMasterBean> investorMasterBeanList = investorMasterService.getAllInvestorByLocationId(locationId,status);
            if (!investorMasterBeanList.isEmpty())
                investorResp = new ResponseEntity<>(investorMasterBeanList, HttpStatus.OK);
            else if (investorMasterBeanList.isEmpty())
                investorResp = new ResponseEntity<>(new Message("Investor list is not available."),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return investorResp;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/id/{id}")
    public ResponseEntity<?> getInvestorById(@PathVariable("id") Long id){
        String status = "active";
        ResponseEntity investorResp = null;
        try {
            InvestorMasterBean investor = investorMasterService.getInvestorById(id,status);
            if (investor != null) {
                investorResp = new ResponseEntity<>(investor, HttpStatus.OK);
            } else if (investor == null) {
                investorResp = new ResponseEntity<>(new Message(id + " id does not exist."), HttpStatus.BAD_REQUEST);
            } else {
                investorResp = new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return investorResp;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/investorCode/{investorCode}")
    public ResponseEntity<?> getInvestorById(@PathVariable("investorCode") String investorCode){
        String status = "active";
        ResponseEntity investorResp = null;
        try {
            InvestorMasterBean investor = investorMasterService.getInvestorByInvestorCode(investorCode,status);
            if (investor != null) {
                investorResp = new ResponseEntity<>(investor, HttpStatus.OK);
            } else if (investor == null) {
                investorResp = new ResponseEntity<>(new Message(investorCode + " code does not exist."), HttpStatus.BAD_REQUEST);
            } else {
                investorResp = new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return investorResp;
    }

    @RequestMapping(method = RequestMethod.POST,value = "")
    public ResponseEntity<?> createInvestorMaster(@Valid @RequestBody InvestorMasterBean investorMasterBean){
        InvestorMasterBean imb = new InvestorMasterBean();
        ResponseEntity investorInsrtResp = null;
        try {

            imb = investorMasterService.createInvestorMaster(investorMasterBean);

            if(imb!=null)
            {
                //meterInsrtResp = new ResponseEntity<>(meterMasterBean.getMeterNumber()+" is created successfully", HttpStatus.OK);
                investorInsrtResp =  new ResponseEntity<>(new Message(imb.getInvestorCode() + " is created successfully."),HttpStatus.OK);
            }else if(imb==null) {
                investorInsrtResp = new ResponseEntity<>(new Message(investorMasterBean.getInvestorCode() + " already exist or something went wrong"), HttpStatus.BAD_REQUEST);
            }else {
                investorInsrtResp = new ResponseEntity<>(new Message("something went wrong"), HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return investorInsrtResp;
    }
}
