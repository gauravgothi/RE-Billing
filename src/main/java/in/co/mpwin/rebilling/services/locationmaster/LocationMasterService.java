package in.co.mpwin.rebilling.services.locationmaster;

import in.co.mpwin.rebilling.beans.locationmaster.LocationMaster;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.repositories.locationmaster.LocationMasterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<String> getDeveloperListByCircleNameAndStatus(String username, String status) {
        return locationMasterRepo.findDeveloperUsernamesByLocationIdAndStatus(username,status);
    }

    public String getIdByCircleNameAndStatus(String circleName, String status) {
        return locationMasterRepo.findIdByCircleNameAndStatus(circleName,status);

    }

    public List<String> getAllRegion(String status) {
        List<String> list = locationMasterRepo.findDistictRegionByStatus(status);
        if(list.isEmpty() || list == null)
            throw new ApiException(HttpStatus.BAD_REQUEST,"Region list are not found in location master.");
            return list;
    }

    public List<String> getAllCircleByRegion(String regionName, String status) {
        List<String> list = locationMasterRepo.findDistictCircleByRegionAndStatus(regionName,status);
        if(list.isEmpty() || list == null)
            throw new ApiException(HttpStatus.BAD_REQUEST,"Circle list are not found in location master.");
        return list;
    }

    public List<LocationMaster> getAllDivisonByCircle(String circleName, String status) {
        List<LocationMaster> list = locationMasterRepo.findDivisionBYCircleAndStatus(circleName,status);
        if(list.isEmpty() || list == null)
            throw new ApiException(HttpStatus.BAD_REQUEST,"Division list are not found in location master.");
        return list;

    }
}
