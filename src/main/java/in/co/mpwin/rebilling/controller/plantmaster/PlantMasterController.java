package in.co.mpwin.rebilling.controller.plantmaster;


import in.co.mpwin.rebilling.beans.plantmaster.PlantMasterBean;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.services.plantmaster.PlantMasterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

            if(plantList.size()>0)
            {
                plantResp = new ResponseEntity<>(plantList, HttpStatus.OK);
            }
            else if(plantList.size()==0)
            {
                plantResp =new ResponseEntity<>(new Message("plant list is not available"),HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return plantResp;
    }


    @RequestMapping(method = RequestMethod.POST,value = "")
    public ResponseEntity<?> createPlantMaster(@Valid @RequestBody PlantMasterBean plantMasterBean){
        PlantMasterBean pmb = new PlantMasterBean();
        ResponseEntity plantInsrtResp = null;
        try {

            pmb = plantMasterService.createPlantMaster(plantMasterBean);

            if(pmb!=null)
            {
               plantInsrtResp =  new ResponseEntity<>(new Message(pmb.getPlantCode() + " is created successfully."),HttpStatus.OK);
            }else if(pmb==null) {
                plantInsrtResp = new ResponseEntity<>(new Message(plantMasterBean.getPlantCode() + " is already exist."), HttpStatus.BAD_REQUEST);
            }else {
                plantInsrtResp = new ResponseEntity<>(new Message("something went wrong"), HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return plantInsrtResp;
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

        } catch (Exception e) {
            e.printStackTrace();
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

        } catch (Exception e) {
            e.printStackTrace();
        }
        return plantResp;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/locationId/{locationId}")
    public ResponseEntity<?> getPlantByLocationId(@PathVariable("locationId") String locationId){
        String status = "active";
        ResponseEntity plantResp = null;
         try {
            List<PlantMasterBean> plantList = plantMasterService.getAllPlantByLocationId(locationId, status);
            if (plantList.size()>0) {
                plantResp = new ResponseEntity<>(plantList , HttpStatus.OK);
            } else if (plantList.size() == 0) {
                plantResp = new ResponseEntity<>(new Message( " plant list does not exist for "+locationId), HttpStatus.BAD_REQUEST);
            } else {
                plantResp = new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return plantResp;
    }

}



