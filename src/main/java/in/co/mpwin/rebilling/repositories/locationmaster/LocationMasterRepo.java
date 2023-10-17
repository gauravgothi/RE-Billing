package in.co.mpwin.rebilling.repositories.locationmaster;

import in.co.mpwin.rebilling.beans.locationmaster.LocationMaster;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

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

    @Query(value = "SELECT developer_username FROM ecell.re_developer_master WHERE location_id = :circleId AND status = :status", nativeQuery = true)
    List<String> findDeveloperUsernamesByLocationIdAndStatus(@Param("circleId") String circleId, @Param("status") String status);

    String findIdByCircleNameAndStatus(String circleName, String status);
}
