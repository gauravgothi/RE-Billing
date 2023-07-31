package in.co.mpwin.rebilling.controller.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterMasterBean;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.metermaster.MeterMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

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
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return meterDtlResp;
    }

    @RequestMapping(method= RequestMethod.POST,value="")
    public ResponseEntity<?> createMeterMaster(@Valid @RequestBody MeterMasterBean meterMasterBean){
        //int result = -1;
        //String resp = null;
        MeterMasterBean mmb = new MeterMasterBean();
        //mmb = null;
        ResponseEntity meterInsrtResp = null;
        try {
            mmb = meterMasterService.createMeterMaster(meterMasterBean);

            if(mmb!=null)
            {
                //meterInsrtResp = new ResponseEntity<>(meterMasterBean.getMeterNumber()+" is created successfully", HttpStatus.OK);
              meterInsrtResp =  new ResponseEntity<>(new Message(mmb.getMeterNumber() + " is created successfully."),HttpStatus.OK);

            }else if(mmb==null) {

                meterInsrtResp = new ResponseEntity<>(new Message(meterMasterBean.getMeterNumber() + " is already exist."), HttpStatus.BAD_REQUEST);
            }else {
                meterInsrtResp = new ResponseEntity<>(new Message("something went wrong"), HttpStatus.BAD_REQUEST);
            }
            return meterInsrtResp;

        }
        /*catch(DataIntegrityViolationException d)
        {
            System.out.println("Personal Message: "+d);
            //d.printStackTrace();
            return new ResponseEntity<>("Meter already exist",HttpStatus.BAD_REQUEST);
        }*/
        catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
            return new ResponseEntity<>("Internal Server Error",HttpStatus.BAD_REQUEST);
        }

    /*@RequestMapping(method= RequestMethod.POST,value="/createMeterMaster")
    public ResponseEntity<?> createMeterMaster(@RequestParam("meterno") String METERNO,
                                                       @RequestParam("make") String MAKE,
                                                       @RequestParam("category") String CATEGORY,
                                                       @RequestParam("type") String TYPE,
                                                       @RequestParam("meter_class") String METER_CLASS,
                                                       @RequestParam("meter_ctr") String METER_CTR,
                                                       @RequestParam("meter_ptr") String METER_PTR,
                                                       @RequestParam("me_ctr") String ME_CTR,
                                                       @RequestParam("me_ptr") String ME_PTR,
                                                       @RequestParam("dial_bmf") String DIAL_BMF,
                                                       @RequestParam("equip_class") String EQUIP_CLASS,
                                                       @RequestParam("phase") String PHASE,
                                                       @RequestParam("metergrp") String METERGRP,
                                                       @RequestParam("mf") String MF,
                                                       @RequestParam("install_date") Date install_date,
                                                       @RequestParam("created_by") String created_by,
                                                       @RequestParam("updated_by") String updated_by,
                                                       @RequestParam("created_on") Timestamp created_on,
                                                       @RequestParam("updated_on") Timestamp updated_on,
                                                       @RequestParam("status") String status,
                                                       @RequestParam("remark") String remark) {
        String resp = null;
        try {

           Integer resp_code = meterMasterService.createMeterMaster(METERNO,
                   MAKE,
                   CATEGORY,
                   TYPE,
                   METER_CLASS,
                   METER_CTR,
                   METER_PTR,
                   ME_CTR,
                   ME_PTR,
                   DIAL_BMF,
                   EQUIP_CLASS,
                   PHASE,
                   METERGRP,
                   MF,
                   install_date,
                   created_by,
                   updated_by,
                   created_on,
                   updated_on,
                   status,
                   remark);

           if(resp_code==1)
           {
               resp = "Success";
           }else {
               resp = "fail";
           }

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }*/




    }
}
