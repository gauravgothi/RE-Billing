package in.co.mpwin.rebilling.controller.locationmaster;

import in.co.mpwin.rebilling.beans.locationmaster.LocationMaster;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.repositories.locationmaster.LocationMasterRepo;
import in.co.mpwin.rebilling.services.locationmaster.LocationMasterService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/location")
@CrossOrigin(origins="*")
public class LocationController {

    @Autowired
    LocationMasterService locationMasterService;

    @Autowired
    private ModelMapper mapper;
    @Autowired
    private LocationMasterRepo locationMasterRepo;

    @RequestMapping(method = RequestMethod.GET, value = "")
    public ResponseEntity<LocationMaster> getAllLocationMaster() {
        String status = "active";
        ResponseEntity locationResp = null;
        try {
            List<LocationMaster> locationList = locationMasterService.findAllByStatus(status);
            if (!locationList.isEmpty()) {
                locationResp = new ResponseEntity<>(locationList, HttpStatus.OK);
            } else if (locationList.isEmpty()) {
                locationResp = new ResponseEntity<>(new Message("Locations list does not exist."), HttpStatus.BAD_REQUEST);
            } else {
                locationResp = new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locationResp;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/division/{division}")
    public ResponseEntity<LocationMaster> getLocationByDivisionCode(@PathVariable("division") String divisionCode) {
        String status = "active";
        ResponseEntity locationResp = null;
        LocationMaster location = null;
        try {
            location = locationMasterService.getLocationByDivisionCode(divisionCode, status);
            if (location != null) {
                locationResp = new ResponseEntity<>(location, HttpStatus.OK);
            } else if (location == null) {
                locationResp = new ResponseEntity<>(new Message("division code does not exist."), HttpStatus.BAD_REQUEST);
            } else {
                locationResp = new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return locationResp;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/id/{id}")
    public ResponseEntity<LocationMaster> getLocationById(@PathVariable("id") String id) {
        String status = "active";
        LocationMaster location = null;
        ResponseEntity locationResp = null;
        try {
            location = locationMasterService.getLocationById(id, status);
            if (location != null) {
                locationResp = new ResponseEntity<>(location, HttpStatus.OK);
            } else if (location == null) {
                locationResp = new ResponseEntity<>(new Message("location id does not exist."), HttpStatus.BAD_REQUEST);
            } else {
                locationResp = new ResponseEntity<>(new Message("Something went wrong."), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locationResp;
    }
}