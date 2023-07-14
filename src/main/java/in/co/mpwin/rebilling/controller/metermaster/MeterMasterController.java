package in.co.mpwin.rebilling.controller.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterMasterBean;
import in.co.mpwin.rebilling.services.metermaster.MeterMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

@RestController
@CrossOrigin(origins="*")
public class MeterMasterController {

    @Autowired
    MeterMasterService meterMasterService;

    @RequestMapping(method= RequestMethod.GET,value="/getMeterDetailsByMeterNo")
    public ResponseEntity<MeterMasterBean> getMeterDetailsByMeterNo(@RequestParam("meterno") String meterno,
                                                                    @RequestParam("status") String status) {
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

    @RequestMapping(method= RequestMethod.GET,value="/getAllMeterByStatus")
    public ResponseEntity<MeterMasterBean> getAllMeterByStatus(@RequestParam("status") String status) {
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

    @RequestMapping(method= RequestMethod.POST,value="/createMeterMaster")
    public ResponseEntity<?> createMeterMaster(@RequestBody MeterMasterBean meterMasterBean){
        int result = -1;
        //String resp = null;
        ResponseEntity meterInsrtResp = null;
        try {
            result = meterMasterService.createMeterMaster(meterMasterBean);

            if(result==1)
            {
                meterInsrtResp = new ResponseEntity<>("Success", HttpStatus.OK);
            }else if(result!=1) {
                meterInsrtResp = new ResponseEntity<>("Fail", HttpStatus.NOT_MODIFIED);
            }else {
                meterInsrtResp = new ResponseEntity<>("Fail", HttpStatus.BAD_REQUEST);
            }

        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
        return meterInsrtResp;

    }
}
