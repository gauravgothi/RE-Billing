package in.co.mpwin.rebilling.services;

import in.co.mpwin.rebilling.beans.LocationMaster;
import in.co.mpwin.rebilling.beans.metermaster.MeterPtr;
import in.co.mpwin.rebilling.repositories.LocationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationService {

    @Autowired
    LocationRepo locationRepo;

    public List<LocationMaster> findAllByStatus(String status){
        List<LocationMaster> locationMasterList = new ArrayList<>();
        try {
            locationMasterList= locationRepo.findAllByStatus(status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locationMasterList;
    }

    public LocationMaster getLocationByDivisionCode(String divisionCode,String status){
        LocationMaster location = new LocationMaster();
        try {
            location= locationRepo.findByDivisionCodeAndStatus(divisionCode,status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public LocationMaster getLocationById(String id,String status){
        LocationMaster location = new LocationMaster();
        try {
            location= locationRepo.findByIdAndStatus(id,status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }


}
