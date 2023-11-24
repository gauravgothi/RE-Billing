package in.co.mpwin.rebilling.controller.mapping;

import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import in.co.mpwin.rebilling.beans.plantmaster.PlantMasterBean;

import in.co.mpwin.rebilling.dto.CompleteMappingDto;

import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.mapping.MeterFeederPlantMappingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(MeterFeederPlantMappingController.class);
    @Autowired
    MeterFeederPlantMappingService meterFeederPlantMappingService;

    @RequestMapping(method = RequestMethod.POST,value = "/mfp")
    public ResponseEntity<?> createMeterFeederPlantMapping(@RequestBody MeterFeederPlantMappingBean meterFeederPlantMappingBean){
        final String methodName = "createMeterFeederPlantMapping() : ";
        logger.info(methodName + "called with parameter meterFeederPlantMappingBean={}",meterFeederPlantMappingBean);
        ResponseEntity resp = null;
        try {
            MeterFeederPlantMappingBean mfpm  = meterFeederPlantMappingService.createNewMapping(meterFeederPlantMappingBean);
            resp =  new ResponseEntity<>(mfpm, HttpStatus.CREATED);
            logger.info(methodName + "return with MeterFeederPlantMappingBean : {} ",mfpm );
            } catch(ApiException apiException) {
            resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch(DataIntegrityViolationException e) {
            resp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
            }catch (NullPointerException ex) {
            resp = new ResponseEntity<>(new Message(ex.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"NullPointerException occurred: {}",ex.getMessage());
            }catch(ParseException ex) {
            resp = new ResponseEntity<>(new Message(ex.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"ParseException occurred: {}",ex.getMessage());
            } catch(Exception e) {
            resp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
            }
        return resp;
    }


    @RequestMapping(method=RequestMethod.GET,value="")
    public ResponseEntity<MeterFeederPlantMappingBean> getAllMapping(){
        final String methodName = "getAllMapping() : ";
        logger.info(methodName + "called with parameter empty");
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
            logger.info(methodName + "return with MeterFeederPlantMapping list of size : {} ",mappingList.size());
        }catch(ApiException apiException) {
            resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch(DataIntegrityViolationException e) {
            resp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
        }catch (NullPointerException ex) {
            resp = new ResponseEntity<>(new Message(ex.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"NullPointerException occurred: {}",ex.getMessage());
        }catch(Exception e) {
            resp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
        }
        return resp;
    }

    @RequestMapping(method=RequestMethod.GET,value="/MFP_Mapping/id/{id}")
    public ResponseEntity<?> getMappingById(@PathVariable("id") Long id){
        final String methodName = "getMappingById() : ";
        logger.info(methodName + "called with parameter id={}",id);
        String status = "active";
        ResponseEntity resp = null;
       MeterFeederPlantMappingBean mappingBean = null;
        try {
            mappingBean = meterFeederPlantMappingService.getMappingById(id, status);
            if ( mappingBean  != null) {
                resp = new ResponseEntity<>( mappingBean, HttpStatus.OK);
            } else if ( mappingBean  == null) {
                resp = new ResponseEntity<>(new Message(id + " id does not exist."), HttpStatus.BAD_REQUEST);
            }
            logger.info(methodName + "return with MeterFeederPlantMappingBean : {} ",mappingBean );

            } catch(ApiException apiException) {
                resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch(DataIntegrityViolationException e) {
                resp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
            }catch (NullPointerException ex) {
                resp = new ResponseEntity<>(new Message(ex.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+"NullPointerException occurred: {}",ex.getMessage());
            }catch(Exception e) {
                resp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
            }
        return resp;
    }
    @RequestMapping(method=RequestMethod.GET,value ="/MFP_Mapping/main_meter_no/{main_meter_no}")
    public ResponseEntity<?> getMappingByMainMeterNo(@PathVariable("main_meter_no") String mmn ){
        final String methodName = "getMappingByMainMeterNo() : ";
        logger.info(methodName + "called with parameter main_meter_no={}",mmn);
        String status = "active";
        ResponseEntity resp = null;
        List<MeterFeederPlantMappingBean> mappingBean = null;
        try {
            mappingBean = meterFeederPlantMappingService.getMappingByMainMeterNo(mmn, status);
            if (mappingBean.size()>0) {
                resp = new ResponseEntity<>( mappingBean, HttpStatus.OK);
            } else if ( mappingBean.size()==0) {
                resp = new ResponseEntity<>(new Message(mmn + " meter no. not found"), HttpStatus.BAD_REQUEST);
            }
            logger.info(methodName + "return with MeterFeederPlantMapping list of size : {} ",mappingBean.size());
            } catch(ApiException apiException) {
                resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch(DataIntegrityViolationException e) {
                resp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
            }catch (NullPointerException ex) {
                resp = new ResponseEntity<>(new Message(ex.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+"NullPointerException occurred: {}",ex.getMessage());
            }catch(Exception e) {
                resp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
            }
        return resp;
    }
    @RequestMapping(method = RequestMethod.GET,value = "/MFP_Mapping/check_meter_no/{check_meter_no}")
    public ResponseEntity<?> getMappingByCheckMeterNo(@PathVariable("check_meter_no") String cmn ){
        final String methodName = "getMappingByCheckMeterNo() : ";
        logger.info(methodName + "called with parameter check_meter_no={}",cmn);
        String status = "active";
        ResponseEntity resp = null;
        List<MeterFeederPlantMappingBean> mappingBean = null;
        try {
            mappingBean = meterFeederPlantMappingService.getMappingByCheckMeterNo(cmn, status);
            if (mappingBean.size()>0) {
                resp = new ResponseEntity<>( mappingBean, HttpStatus.OK);
            } else if ( mappingBean.size()==0) {
                resp = new ResponseEntity<>(new Message(cmn + " meter no. not found"), HttpStatus.BAD_REQUEST);
            }
            logger.info(methodName + "return with MeterFeederPlantMapping list of size : {} ",mappingBean.size());

            } catch(ApiException apiException) {
                resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch(DataIntegrityViolationException e) {
                resp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
            }catch (NullPointerException ex) {
                resp = new ResponseEntity<>(new Message(ex.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+"NullPointerException occurred: {}",ex.getMessage());
            }catch(Exception e) {
                resp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
            }
        return resp;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/MFP_Mapping/standby_meter_no/{standby_meter_no}")
    public ResponseEntity<?> getMappingByStandbyMeterNo(@PathVariable("standby_meter_no") String smn ){
        final String methodName = "getMappingByStandbyMeterNo() : ";
        logger.info(methodName + "called with parameter standby_meter_no={}",smn);
        String status = "active";
        ResponseEntity resp = null;
        List<MeterFeederPlantMappingBean> mappingBean = null;
        try {
            mappingBean = meterFeederPlantMappingService.getMappingByStandbyMeterNo(smn, status);
            if (mappingBean.size()>0) {
                resp = new ResponseEntity<>( mappingBean, HttpStatus.OK);
            } else if (mappingBean.size()==0) {
                resp = new ResponseEntity<>(new Message(smn + " meter no. not found"), HttpStatus.BAD_REQUEST);
            }
            logger.info(methodName + "return with MeterFeederPlantMapping list of size : {} ",mappingBean.size());
        } catch(ApiException apiException) {
            resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch(DataIntegrityViolationException e) {
            resp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
        }catch (NullPointerException ex) {
            resp = new ResponseEntity<>(new Message(ex.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"NullPointerException occurred: {}",ex.getMessage());
        }catch(Exception e) {
            resp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
        }
        return resp;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/MFP_Mapping/developer_id/{developer_id}")
    public ResponseEntity<?> getMappingByDeveloperId(@PathVariable("developer_id") String dID ){
        final String methodName = "getMappingByDeveloperId() : ";
        logger.info(methodName + "called with parameter developer_id={}",dID);
        String status = "active";
        ResponseEntity resp = null;
        List<MeterFeederPlantMappingBean> mappingBean = null;
        try {
            mappingBean = meterFeederPlantMappingService.getMappingByDeveloperId(dID, status);
            if ( mappingBean.size()>0) {
                resp = new ResponseEntity<>( mappingBean, HttpStatus.OK);
            } else if (mappingBean.size()==0) {
                resp = new ResponseEntity<>(new Message(dID + " id does not exist."), HttpStatus.BAD_REQUEST);
            }
            logger.info(methodName + "return with MeterFeederPlantMapping list of size : {} ",mappingBean.size());
            } catch(ApiException apiException) {
                resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch(DataIntegrityViolationException e) {
                resp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
            }catch (NullPointerException ex) {
                resp = new ResponseEntity<>(new Message(ex.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+"NullPointerException occurred: {}",ex.getMessage());
            }catch(Exception e) {
                resp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
            }
        return resp;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/MFP_Mapping/feeder_code/{feeder_code}")
    public ResponseEntity<?> getMappingByFeederCode(@PathVariable("feeder_code") String fcode ){
        final String methodName = "getMappingByFeederCode() : ";
        logger.info(methodName + "called with parameter feeder_code={}",fcode);
        String status = "active";
        ResponseEntity resp = null;
        List<MeterFeederPlantMappingBean> mappingBean = null;
        try {
            mappingBean = meterFeederPlantMappingService.getMappingByFeederCode(fcode, status);
            if (mappingBean.size()>0) {
                resp = new ResponseEntity<>( mappingBean, HttpStatus.OK);
            } else if (mappingBean.size()==0) {
                resp = new ResponseEntity<>(new Message(fcode + " id does not exist."), HttpStatus.BAD_REQUEST);
            }
            logger.info(methodName + "return with MeterFeederPlantMapping list of size : {} ",mappingBean.size());
            } catch(ApiException apiException) {
                resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch(DataIntegrityViolationException e) {
                resp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
            }catch (NullPointerException ex) {
                resp = new ResponseEntity<>(new Message(ex.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+"NullPointerException occurred: {}",ex.getMessage());
            }catch(Exception e) {
                resp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
            }
        return resp;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/MFP_Mapping/plant_code/{plant_code}")
    public ResponseEntity<?> getMappingByPlantCode(@PathVariable("plant_code") String plantCode ){
        final String methodName = "getMappingByPlantCode() : ";
        logger.info(methodName + "called with parameter plantCode={}",plantCode);
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
            logger.info(methodName + "return with MeterFeederPlantMapping list of size : {} ",mappingBean.size());
            } catch(ApiException apiException) {
                resp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
                logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
            } catch(DataIntegrityViolationException e) {
                resp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
            }catch (NullPointerException ex) {
                resp = new ResponseEntity<>(new Message(ex.getMessage()), HttpStatus.BAD_REQUEST);
                logger.error(methodName+"NullPointerException occurred: {}",ex.getMessage());
            }catch(Exception e) {
                resp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
                logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
            }
        return resp;
    }


    @GetMapping("/view-complete/meter/{meter}")
    public ResponseEntity<?> getCompleteMappingByMeterNumber(@PathVariable("meter") String meterNumber){
        final String methodName = "getCompleteMappingByMeterNumber() : ";
        logger.info(methodName + "called with parameter meterNumber={}",meterNumber);
        ResponseEntity viewMappingDtoResp;
        try {
                CompleteMappingDto completeMappingDto =
                        meterFeederPlantMappingService.getCompleteMappingByMeterNumber(meterNumber);
                viewMappingDtoResp = new ResponseEntity<>(completeMappingDto,HttpStatus.OK);
            logger.info(methodName + "return with CompleteMappingDto : {} ",completeMappingDto );

        }catch(ApiException apiException) {
            viewMappingDtoResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            logger.error(methodName+" API Exception occurred: {}", apiException.getMessage());
        } catch(DataIntegrityViolationException e) {
            viewMappingDtoResp = new ResponseEntity<>(new Message("Data Integrity Violation Exception occurred : "+e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+"Data Integrity Violation Exception occurred: {}",e.getMessage());
        }catch (NullPointerException ex) {
            viewMappingDtoResp = new ResponseEntity<>(new Message(ex.getMessage()), HttpStatus.BAD_REQUEST);
            logger.error(methodName+"NullPointerException occurred: {}",ex.getMessage());
        }catch(Exception e) {
            viewMappingDtoResp = new ResponseEntity<>(new Message("Exception occurred : " +e.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}",e.getMessage(),e);
        }
        return viewMappingDtoResp;
    }

}
