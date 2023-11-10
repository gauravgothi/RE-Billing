package in.co.mpwin.rebilling.controller.plantmaster;


import in.co.mpwin.rebilling.beans.plantmaster.PlantMasterBean;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.plantmaster.PlantMasterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plant")
@CrossOrigin(origins="*")
public class PlantMasterController {

    @Autowired
    PlantMasterService plantMasterService;
    @RequestMapping(method= RequestMethod.GET,value="")
    public ResponseEntity<PlantMasterBean> getAllPlantMaster(){
        ResponseEntity plantResp = null;
        try {
            String status = "active";
            List<PlantMasterBean> plantList = plantMasterService.getAllPlantMasterBean(status);
            plantResp = new ResponseEntity<>(plantList, HttpStatus.OK);
            }catch (ApiException apiException) {
                plantResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
            } catch (DataIntegrityViolationException e) {
                plantResp =  new ResponseEntity<>(new Message("Data Integrity Violation : "+e.getMessage().substring(0, e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
            }catch (Exception e) {
                e.printStackTrace();
                plantResp = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
            }
            return plantResp;
    }


    @RequestMapping(method = RequestMethod.POST,value = "")
    public ResponseEntity<?> createPlantMaster(@Valid @RequestBody PlantMasterBean plantMasterBean){
        PlantMasterBean pmb = new PlantMasterBean();
        ResponseEntity resp = null;
        try {

            pmb = plantMasterService.createPlantMaster(plantMasterBean);

            if(pmb!=null)
            {
                resp =  new ResponseEntity<>(new Message(pmb.getPlantCode() + " is created successfully."),HttpStatus.OK);
            }else if(pmb==null) {
                resp = new ResponseEntity<>(new Message(" plant could not created due to some error."), HttpStatus.BAD_REQUEST);
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


    @RequestMapping(method = RequestMethod.GET,value = "/plantCode/{plantCode}")
    public ResponseEntity<?> getPlantByPlantCode(@PathVariable("plantCode") String plantCode){
        String status = "active";
        ResponseEntity plantResp = null;
        PlantMasterBean plantBean = null;
        try {
            plantBean = plantMasterService.getPlantByPlantCode(plantCode, status);
            if (plantBean!= null) {
                plantResp = new ResponseEntity<>(plantBean, HttpStatus.OK);
            } else if (plantBean == null) {
                plantResp = new ResponseEntity<>(new Message(plantCode + " number does not exist."), HttpStatus.BAD_REQUEST);
            } else {
                plantResp= new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }

        } catch (ApiException apiException) {
            plantResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException e) {
            plantResp = new ResponseEntity<>(new Message("Data Integrity Violation : "+e.getMessage().substring(0, e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            plantResp = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return plantResp;
    }


    @RequestMapping(method = RequestMethod.GET,value = "/id/{id}")
    public ResponseEntity<?> getPlantById(@PathVariable("id") Long id){
        String status = "active";
        ResponseEntity plantResp = null;
        PlantMasterBean plantBean = null;
        try {
            plantBean = plantMasterService.getPlantById(id, status);
            if (plantBean  != null) {
                plantResp = new ResponseEntity<>(plantBean , HttpStatus.OK);
            } else if (plantBean  == null) {
                plantResp = new ResponseEntity<>(new Message(id + " id does not exist."), HttpStatus.BAD_REQUEST);
            } else {
                plantResp = new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }

        } catch (ApiException apiException) {
            plantResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException e) {
            plantResp = new ResponseEntity<>(new Message("Data Integrity Violation : "+e.getMessage().substring(0, e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            plantResp = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return plantResp;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/locationId/{locationId}")
    public ResponseEntity<?> getPlantByLocationId(@PathVariable("locationId") String locationId){
        String status = "active";
        ResponseEntity plantResp = null;
         try {
            List<PlantMasterBean> plantList = plantMasterService.getAllPlantByLocationId(locationId, status);
            if (plantList.size()>0)
                plantResp = new ResponseEntity<>(plantList , HttpStatus.OK);
             else if (plantList.size() == 0)
                throw new ApiException(HttpStatus.BAD_REQUEST, "plant list are not available for this location id:"+locationId);
             else
                throw new ApiException(HttpStatus.BAD_REQUEST, "something went wrong to get plant list "+locationId);
            } catch (ApiException apiException) {
             plantResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
         } catch (DataIntegrityViolationException e) {
             plantResp = new ResponseEntity<>(new Message("Data Integrity Violation : "+e.getMessage().substring(0, e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
         }catch (Exception e) {
             e.printStackTrace();
             plantResp = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
         }
        return plantResp;
    }

    @RequestMapping(method= RequestMethod.GET,value="/list")
    public ResponseEntity<PlantMasterBean> getAllPlantMasterForMfpMapping(){
        ResponseEntity plantResp = null;
        try {
            String status = "active";
            List<PlantMasterBean> plantList = plantMasterService.getAllPlantMasterBean(status);
            plantResp = new ResponseEntity<>(plantList, HttpStatus.OK);
        }catch (ApiException apiException) {
            plantResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException e) {
            plantResp = new ResponseEntity<>(new Message("Data Integrity Violation : "+e.getMessage().substring(0, e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            e.printStackTrace();
            plantResp = new ResponseEntity<>(new Message("Exception: " +e.getMessage().substring(0, e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return plantResp;
    }

    // to get the plant list for investor machine mapping where plant should exit in mfp mapping with developer
    @RequestMapping(method= RequestMethod.GET,value="/list/developerId/{developerId}")
    public ResponseEntity<PlantMasterBean> getAllPlantMasterForInvestorMachineMapping(@PathVariable String developerId){
        ResponseEntity plantResp = null;
        try {
            List<PlantMasterBean> plantList = plantMasterService.getAllPlantMasterBeanByDeveloperId(developerId);
            plantResp = new ResponseEntity<>(plantList, HttpStatus.OK);
        }catch (ApiException apiException) {
            plantResp = new ResponseEntity<>(new Message(apiException.getMessage()), apiException.getHttpStatus());
        } catch (DataIntegrityViolationException e) {
            plantResp = new ResponseEntity<>(new Message("Data Integrity Violation : "+e.getMessage().substring(0, e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            e.printStackTrace();
            plantResp = new ResponseEntity<>(new Message("Exception: " + e.getMessage().substring(0, e.getMessage().indexOf("Detail"))), HttpStatus.BAD_REQUEST);
        }
        return plantResp;
    }


}



