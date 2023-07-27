package in.co.mpwin.rebilling.repositories;

import in.co.mpwin.rebilling.beans.LocationMaster;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface LocationRepo extends CrudRepository<LocationMaster,String> {

    public List<LocationMaster> findAllByStatus(String status);

    LocationMaster findByIdAndStatus(Long id,String Status);

//    public List<LocationMaster> findDistinctRegionByStatus(String status);
//    public List<LocationMaster> findDistinctCircleByRegionAndStatus(String region,String status);
//
//    public List<LocationMaster> findDistinctDivisionByCircleAndStatus(String circle,String status);
//
//    public List<LocationMaster> findDistinctDcNameByDivisionAndStatus(String division, String status);


    LocationMaster findByDivisionCodeAndStatus(String divisionCode, String status);
}
