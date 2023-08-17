package in.co.mpwin.rebilling.controller;

import in.co.mpwin.rebilling.beans.LocationMaster;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.repositories.LocationRepo;
import in.co.mpwin.rebilling.services.LocationService;
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
    LocationService locationService;

    @Autowired
    private ModelMapper mapper;
    @Autowired
    private LocationRepo locationRepo;

    @RequestMapping(method = RequestMethod.GET, value = "")
    public ResponseEntity<LocationMaster> getAllLocationMaster() {
        String status = "active";
        ResponseEntity locationResp = null;
        try {
            List<LocationMaster> locationList = locationService.findAllByStatus(status);
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
            location = locationService.getLocationByDivisionCode(divisionCode, status);
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
            location = locationService.getLocationById(id, status);
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