package in.co.mpwin.rebilling.controller.mapping;

import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import in.co.mpwin.rebilling.beans.plantmaster.PlantMasterBean;

import in.co.mpwin.rebilling.dto.CompleteMappingDto;

import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.mapping.MeterFeederPlantMappingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/mapping")
@CrossOrigin(origins="*")
@Validated
public class MeterFeederPlantMappingController {
    @Autowired
    MeterFeederPlantMappingService meterFeederPlantMappingService;

    @RequestMapping(method = RequestMethod.POST,value = "/mfp")
    public ResponseEntity<?> createMeterFeederPlantMapping(@RequestBody MeterFeederPlantMappingBean meterFeederPlantMappingBean){
        ResponseEntity resp = null;
        try {
            MeterFeederPlantMappingBean mfpm  = meterFeederPlantMappingService.createNewMapping(meterFeederPlantMappingBean);
            resp =  new ResponseEntity<>(mfpm, HttpStatus.CREATED);
            } catch (ApiException apiException) {
            resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            } catch (DataIntegrityViolationException d) {
            resp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
            }catch (NullPointerException ex) {
            resp = new ResponseEntity<>(new Message(ex.getMessage().substring(0,250)), HttpStatus.BAD_REQUEST);
            }catch(ParseException ex)
            {
                resp = new ResponseEntity<>(new Message(ex.getMessage().substring(0,250)), HttpStatus.BAD_REQUEST);
            }catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
             }
        return resp;
    }


    @RequestMapping(method=RequestMethod.GET,value="")
    public ResponseEntity<MeterFeederPlantMappingBean> getAllMapping(){
        ResponseEntity resp = null;
        try {
            String status = "active";
            List<MeterFeederPlantMappingBean> mappingList = meterFeederPlantMappingService.getAllMapping(status);

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

    @RequestMapping(method=RequestMethod.GET,value="/MFP_Mapping/id/{id}")
    public ResponseEntity<?> getMappingId(@PathVariable("id") Long id){
        String status = "active";
        ResponseEntity resp = null;
       MeterFeederPlantMappingBean mappingBean = null;
        try {
            mappingBean = meterFeederPlantMappingService.getMappingById(id, status);
            if ( mappingBean  != null) {
                resp = new ResponseEntity<>( mappingBean, HttpStatus.OK);
            } else if ( mappingBean  == null) {
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
    @RequestMapping(method=RequestMethod.GET,value ="/MFP_Mapping/main_meter_no/{main_meter_no}")
    public ResponseEntity<?> getMappingByMainMeterNo(@PathVariable("main_meter_no") String mmn ){
        String status = "active";
        ResponseEntity resp = null;
        List<MeterFeederPlantMappingBean> mappingBean = null;
        try {
            mappingBean = meterFeederPlantMappingService.getMappingByMainMeterNo(mmn, status);
            if (mappingBean.size()>0) {
                resp = new ResponseEntity<>( mappingBean, HttpStatus.OK);
            } else if ( mappingBean.size()==0) {
                resp = new ResponseEntity<>(new Message(mmn + " meter no. not found"), HttpStatus.BAD_REQUEST);
            } else {
                resp = new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return resp;
    }
    @RequestMapping(method = RequestMethod.GET,value = "/MFP_Mapping/check_meter_no/{check_meter_no}")
    public ResponseEntity<?> getMappingByCheckMeterNo(@PathVariable("check_meter_no") String cmn ){
        String status = "active";
        ResponseEntity resp = null;
        List<MeterFeederPlantMappingBean> mappingBean = null;
        try {
            mappingBean = meterFeederPlantMappingService.getMappingByCheckMeterNo(cmn, status);
            if (mappingBean.size()>0) {
                resp = new ResponseEntity<>( mappingBean, HttpStatus.OK);
            } else if ( mappingBean.size()==0) {
                resp = new ResponseEntity<>(new Message(cmn + " meter no. not found"), HttpStatus.BAD_REQUEST);
            } else {
                resp = new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return resp;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/MFP_Mapping/standby_meter_no/{standby_meter_no}")
    public ResponseEntity<?> getMappingByStandbyMeterNo(@PathVariable("standby_meter_no") String smn ){
        String status = "active";
        ResponseEntity resp = null;
        List<MeterFeederPlantMappingBean> mappingBean = null;
        try {
            mappingBean = meterFeederPlantMappingService.getMappingByStandbyMeterNo(smn, status);
            if (mappingBean.size()>0) {
                resp = new ResponseEntity<>( mappingBean, HttpStatus.OK);
            } else if (mappingBean.size()==0) {
                resp = new ResponseEntity<>(new Message(smn + " meter no. not found"), HttpStatus.BAD_REQUEST);
            } else {
                resp = new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return resp;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/MFP_Mapping/developer_id/{developer_id}")
    public ResponseEntity<?> getMappingByDeveloperId(@PathVariable("developer_id") String di ){
        String status = "active";
        ResponseEntity resp = null;
        List<MeterFeederPlantMappingBean> mappingBean = null;
        try {
            mappingBean = meterFeederPlantMappingService.getMappingByDeveloperId(di, status);
            if ( mappingBean.size()>0) {
                resp = new ResponseEntity<>( mappingBean, HttpStatus.OK);
            } else if (mappingBean.size()==0) {
                resp = new ResponseEntity<>(new Message(di + " id does not exist."), HttpStatus.BAD_REQUEST);
            } else {
                resp = new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return resp;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/MFP_Mapping/feeder_code/{feeder_code}")
    public ResponseEntity<?> getMappingByFeederCode(@PathVariable("feeder_code") String fcode ){
        String status = "active";
        ResponseEntity resp = null;
        List<MeterFeederPlantMappingBean> mappingBean = null;
        try {
            mappingBean = meterFeederPlantMappingService.getMappingByFeederCode(fcode, status);
            if (mappingBean.size()>0) {
                resp = new ResponseEntity<>( mappingBean, HttpStatus.OK);
            } else if (mappingBean.size()==0) {
                resp = new ResponseEntity<>(new Message(fcode + " id does not exist."), HttpStatus.BAD_REQUEST);
            } else {
                resp = new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return resp;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/MFP_Mapping/plant_code/{plant_code}")
    public ResponseEntity<?> getMappingByPlantCode(@PathVariable("plant_code") String plantCode ){
        String status = "active";
        ResponseEntity resp = null;
        List<MeterFeederPlantMappingBean> mappingBean = null;
        try {
            mappingBean = meterFeederPlantMappingService.getMappingByPlantCode(plantCode, status);
            if (mappingBean.size()>0) {
                resp = new ResponseEntity<>( mappingBean, HttpStatus.OK);
            } else if (mappingBean.size()==0) {
                resp = new ResponseEntity<>(new Message(plantCode + " id does not exist."), HttpStatus.BAD_REQUEST);
            } else {
                resp = new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseEntity<>(new Message(e.getMessage().substring(0,e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return resp;
    }


    @GetMapping("/view-complete/meter/{meter}")
    public ResponseEntity<?> getCompleteMappingByMeterNumber(@PathVariable("meter") String meterNumber){
        ResponseEntity viewMappingDtoResp;
        try {
                CompleteMappingDto completeMappingDto =
                        meterFeederPlantMappingService.getCompleteMappingByMeterNumber(meterNumber);
                viewMappingDtoResp = new ResponseEntity<>(completeMappingDto,HttpStatus.OK);
        }catch (ApiException apiException){
            viewMappingDtoResp = new ResponseEntity<>(new Message(apiException.getMessage()),apiException.getHttpStatus());
        }catch (DataIntegrityViolationException d){
            viewMappingDtoResp = new ResponseEntity<>(new Message("Data Integrity Violation"), HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            viewMappingDtoResp = new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return viewMappingDtoResp;
    }

}
