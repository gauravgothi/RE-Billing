package in.co.mpwin.rebilling.repositories.locationmaster;

import in.co.mpwin.rebilling.beans.locationmaster.LocationMaster;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LocationMasterRepo extends CrudRepository<LocationMaster,String> {

    public List<LocationMaster> findAllByStatus(String status);

    LocationMaster findByIdAndStatus(String id,String Status);

//    public List<LocationMaster> findDistinctRegionByStatus(String status);
//    public List<LocationMaster> findDistinctCircleByRegionAndStatus(String region,String status);
//
//    public List<LocationMaster> findDistinctDivisionByCircleAndStatus(String circle,String status);
//
//    public List<LocationMaster> findDistinctDcNameByDivisionAndStatus(String division, String status);


    LocationMaster findByDivisionCodeAndStatus(String divisionCode, String status);
}
