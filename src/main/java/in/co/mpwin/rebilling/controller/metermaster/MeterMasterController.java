package in.co.mpwin.rebilling.controller.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterMasterBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.metermaster.MeterMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/meter")
@CrossOrigin(origins="*")
public class MeterMasterController {

    @Autowired
    MeterMasterService meterMasterService;

    @RequestMapping(method= RequestMethod.GET,value="/meterno/{meterno}/status/{status}")
    public ResponseEntity<MeterMasterBean> getMeterDetails(@PathVariable("meterno") String meterno,
                                                                    @PathVariable("status") String status) {
        ResponseEntity meterDtlResp = null;
        try {

            MeterMasterBean meterMasterBean = new MeterMasterBean();
            meterMasterBean = meterMasterService.getMeterDetailsByMeterNo(meterno, status);
            if(meterMasterBean!=null)
            {
                meterDtlResp = new ResponseEntity<>(meterMasterBean, HttpStatus.OK);
            }
            else if(meterMasterBean==null)
            {
                meterDtlResp=new ResponseEntity<>("Meter Detail not present",HttpStatus.NO_CONTENT);
            }
            else {
                meterDtlResp=new ResponseEntity<>("Invalid Request",HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return meterDtlResp;
    }

    @RequestMapping(method= RequestMethod.GET,value="/status/{status}")
    public ResponseEntity<MeterMasterBean> getAllMeterByStatus(@PathVariable("status") String status) {
        ResponseEntity meterDtlResp = null;
        try {

            ArrayList<MeterMasterBean> meterList = meterMasterService.getAllMeterByStatus(status);
            if(meterList.size()>0)
            {
                meterDtlResp = new ResponseEntity<>(meterList, HttpStatus.OK);
            }
            else if(meterList.size()==0)
            {
                meterDtlResp=new ResponseEntity<>("Meter Detail not present",HttpStatus.NO_CONTENT);
            }
            else {
                meterDtlResp=new ResponseEntity<>("Invalid Request",HttpStatus.BAD_REQUEST);
            }
          } catch (DataIntegrityViolationException d)
           {
            Throwable rootCause = d.getRootCause();
            String msg=rootCause.getMessage().substring(0,rootCause.getMessage().indexOf("Detail:"));
               meterDtlResp = new ResponseEntity<>(new Message(msg),HttpStatus.INTERNAL_SERVER_ERROR);
           } catch (Exception e)
           {
               meterDtlResp = new ResponseEntity<>(new Message("something went wrong or some exception occurred "+e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);

           }
        return meterDtlResp;
    }

    @RequestMapping(method= RequestMethod.POST,value="")
    public ResponseEntity<?> createMeterMaster(@Valid @RequestBody MeterMasterBean meterMasterBean) {
        //int result = -1;
        //String resp = null;
        MeterMasterBean mmb = new MeterMasterBean();
        //mmb = null;
        ResponseEntity meterInsrtResp = null;
        try {
            mmb = meterMasterService.createMeterMaster(meterMasterBean);

            if (mmb != null) {
                //meterInsrtResp = new ResponseEntity<>(meterMasterBean.getMeterNumber()+" is created successfully", HttpStatus.OK);
                meterInsrtResp = new ResponseEntity<>(new Message(mmb.getMeterNumber() + " is created successfully."), HttpStatus.OK);

            } else if (mmb == null) {

                meterInsrtResp = new ResponseEntity<>(new Message(meterMasterBean.getMeterNumber() + " is already exist."), HttpStatus.BAD_REQUEST);
            } else {
                meterInsrtResp = new ResponseEntity<>(new Message("something went wrong"), HttpStatus.BAD_REQUEST);
            }
            return meterInsrtResp;

        }
        /*catch(DataIntegrityViolationException d)
        {
            System.out.println("Personal Message: "+d);
            //d.printStackTrace();
            return new ResponseEntity<>("Meter already exist",HttpStatus.BAD_REQUEST);
        }*/ catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            return new ResponseEntity<>("Internal Server Error", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/list/byUser")
    public ResponseEntity<?> getMetersByUser(){
        ResponseEntity meterListResp  = null;
        try {
                List<Map<String,String>> meterList = meterMasterService.getMetersByUser();
                meterListResp = new ResponseEntity<>(meterList,HttpStatus.OK);
        }catch (ApiException apiException){
            meterListResp = new ResponseEntity<>(new Message(apiException.getMessage()),apiException.getHttpStatus());
        }catch (DataIntegrityViolationException d){
            Throwable rootCause = d.getRootCause();
            String msg=rootCause.getMessage().substring(0,rootCause.getMessage().indexOf("Detail:"));
            meterListResp = new ResponseEntity<>(new Message(msg),HttpStatus.INTERNAL_SERVER_ERROR);
            return meterListResp;
        }catch (Exception e){
            meterListResp = new ResponseEntity<>(new Message(" something went wrong or some exception occurred "),HttpStatus.INTERNAL_SERVER_ERROR);
            return meterListResp;
        }
        return meterListResp;
    }

    @GetMapping(value = "/list")
    public ResponseEntity<?> getMeters(){
        ResponseEntity meterListResp  = null;
        try {
            List<Map<String,String>> meterList = meterMasterService.getMeters();
            meterListResp = new ResponseEntity<>(meterList,HttpStatus.OK);
        }catch (ApiException apiException){
            meterListResp = new ResponseEntity<>(new Message(apiException.getMessage()),apiException.getHttpStatus());
        }catch (DataIntegrityViolationException d){
            Throwable rootCause = d.getRootCause();
            String msg=rootCause.getMessage().substring(0,rootCause.getMessage().indexOf("Detail:"));
            meterListResp = new ResponseEntity<>(new Message(msg),HttpStatus.INTERNAL_SERVER_ERROR);
            return meterListResp;
        }catch (Exception e){
            meterListResp = new ResponseEntity<>(new Message(" something went wrong or some exception occurred "),HttpStatus.INTERNAL_SERVER_ERROR);
            return meterListResp;
        }
        return meterListResp;
    }
}
