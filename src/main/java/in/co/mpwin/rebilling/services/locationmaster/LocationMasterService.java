package in.co.mpwin.rebilling.services.locationmaster;

import in.co.mpwin.rebilling.beans.locationmaster.LocationMaster;
import in.co.mpwin.rebilling.repositories.locationmaster.LocationMasterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationMasterService {

    @Autowired
    LocationMasterRepo locationMasterRepo;

    public List<LocationMaster> findAllByStatus(String status){
        List<LocationMaster> locationMasterList = new ArrayList<>();
        try {
            locationMasterList= locationMasterRepo.findAllByStatus(status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locationMasterList;
    }

    public LocationMaster getLocationByDivisionCode(String divisionCode,String status){
        LocationMaster location = new LocationMaster();
        try {
            location= locationMasterRepo.findByDivisionCodeAndStatus(divisionCode,status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public LocationMaster getLocationById(String id,String status){
        LocationMaster location = new LocationMaster();
        try {
            location= locationMasterRepo.findByIdAndStatus(id,status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }


}
