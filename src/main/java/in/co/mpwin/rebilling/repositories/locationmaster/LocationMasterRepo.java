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
    @Query(value = "SELECT distinct(region_name) FROM ecell.re_location_master WHERE status = :status", nativeQuery = true)
    List<String> findDistictRegionByStatus(@Param("status") String status);
    @Query(value = "SELECT distinct(circle_name) FROM ecell.re_location_master WHERE region_name=:regionName AND status = :status", nativeQuery = true)
    List<String> findDistictCircleByRegionAndStatus(@Param("regionName") String regionName,@Param("status") String status);

    @Query(value = "SELECT * FROM ecell.re_location_master WHERE circle_name=:circleName AND status = :status", nativeQuery = true)
    List<LocationMaster> findDivisionBYCircleAndStatus(@Param("circleName") String circleName,@Param("status") String status);
}
