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

    public List<LocationMaster> findDistinctRegion(String status){
        List<LocationMaster> regionList = new ArrayList<>();
        try {
            regionList = locationRepo.findDistinctRegionByStatus(status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return regionList;
    }

    public List<LocationMaster> getDistinctCircleByRegionAndStatus(String region,String status){
        List<LocationMaster> circleList = new ArrayList<>();

        try {
            circleList= locationRepo.findDistinctCircleByRegionAndStatus(region,status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return circleList;
    }

    public List<LocationMaster> getDistinctDivisionByCircleAndStatus(String circle,String status){
        List<LocationMaster> divisionList = new ArrayList<>();


        try {
            divisionList= locationRepo.findDistinctDivisionByCircleAndStatus(circle,status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return divisionList;
    }

    public List<LocationMaster> getDistinctDcNameByDivisionAndStatus(String division,String status){
        List<LocationMaster> dcNameList = new ArrayList<>();

        try {
            dcNameList= locationRepo.findDistinctDcNameByDivisionAndStatus(division,status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dcNameList;
    }


}
