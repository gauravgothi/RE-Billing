package in.co.mpwin.rebilling.repositories.mapping;

import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import in.co.mpwin.rebilling.services.mapping.MeterFeederPlantMappingService;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface MeterFeederPlantMappingRepo extends CrudRepository<MeterFeederPlantMappingBean,Long> {

    @Query(value = "select * from ecell.re_meter_feeder_plant_mapping where main_meter_no = ?1 and check_meter_no=?2 and standby_meter_no=?3 and developer_id=?4", nativeQuery = true)
    MeterFeederPlantMappingBean findByMainMeterNoCheckMeterNoStandbyMeterNoAndDeveloperId(String mainMeterNo, String checkMeterNo, String standbyMeterNo, String developerId);
    MeterFeederPlantMappingBean save(MeterFeederPlantMappingBean meterFeederPlantMappingBean);

    MeterFeederPlantMappingBean findByIdAndStatus(Long id, String status);

    List<MeterFeederPlantMappingBean>  findByMainMeterNoAndStatus(String mmn, String status);

    List<MeterFeederPlantMappingBean> findByCheckMeterNoAndStatus(String mmn, String status);

    List<MeterFeederPlantMappingBean> findByStandbyMeterNoAndStatus(String smn, String status);

    @Query(value = "SELECT * from ecell.re_meter_feeder_plant_mapping WHERE status =:status AND (main_meter_no=:meterNo OR check_meter_no =:meterNo) ORDER BY id DESC LIMIT 1;",
        nativeQuery = true)
    MeterFeederPlantMappingBean findByAnyMeterNoAndStatus(@Param("meterNo") String meterNo,
                                            @Param("status") String status);


    List<MeterFeederPlantMappingBean> findByDeveloperIdAndStatus(String di, String status);

    List<MeterFeederPlantMappingBean>findByFeederCodeAndStatus(String fcode, String status);

    List<MeterFeederPlantMappingBean> findByPlantCodeAndStatus(String plantCode, String status);

    List<MeterFeederPlantMappingBean> findByStatus(String status);


    @Query(value = "select * from ecell.re_meter_feeder_plant_mapping where main_meter_no=:meterNo and status=:status order by id DESC limit 1", nativeQuery = true)
    MeterFeederPlantMappingBean findLastMFPMappingByMainMeterNo(@Param("meterNo") String meterNo, @Param("status") String status);

    @Query(value = "select * from ecell.re_meter_feeder_plant_mapping where check_meter_no=:meterNo and status=:status order by id DESC limit 1", nativeQuery = true)
    MeterFeederPlantMappingBean findLastMFPMappingByCheckMeterNo(@Param("meterNo") String meterNo, @Param("status") String status);

    @Query(value = "select * from ecell.re_meter_feeder_plant_mapping where standby_meter_no=:meterNo and status=:status order by id DESC limit 1", nativeQuery = true)
    MeterFeederPlantMappingBean findLastMFPMappingByStandbyMeterNo(@Param("meterNo") String meterNo, @Param("status") String status);

    @Modifying
    @Transactional
    @Query(value = "UPDATE ecell.re_meter_feeder_plant_mapping SET end_date=:replaceDate where id=:id", nativeQuery = true)
    void updateMappingEndDatebyId(@Param("id") Long id,@Param("replaceDate") Date replaceDate);

    @Query(value = "select distinct t.plant_code from ecell.re_meter_feeder_plant_mapping t where t.developer_id=:developerId and t.status=:status order by t.plant_code",nativeQuery = true)
    List<String> findDistinctPlantCodeByDeveloperIdAndStatus(@Param("developerId") String developerId,@Param("status") String status);

    List<MeterFeederPlantMappingBean> findAllByDeveloperIdAndStatusOrderByEndDateAsc(String di, String status);

    @Query(value = "select * from ecell.re_meter_feeder_plant_mapping where developer_id=:developerId and plant_code=:plantId and status=:status order by id DESC limit 1", nativeQuery = true)
    MeterFeederPlantMappingBean findByDeveloperIdAndPlantIdAndStatus(@Param("developerId") String developerId,@Param("plantId") String plantId, @Param("status") String status);

    @Query(value = "select * from ecell.re_meter_feeder_plant_mapping where end_date>:endDate AND status=:status", nativeQuery = true)
    List<MeterFeederPlantMappingBean> findMappedMeterListByEndDate(@Param("endDate") LocalDate endDate,@Param("status") String status );

    @Query(value = "select * from ecell.re_meter_feeder_plant_mapping where main_meter_no=:meterNumber AND end_date>:endDate AND status=:status", nativeQuery = true)
    MeterFeederPlantMappingBean findByMainMeterNoAndEndDateAndStatus(@Param("meterNumber")  String meterNumber,@Param("endDate") LocalDate endDate, @Param("status") String status);
    @Query(value = "select * from ecell.re_meter_feeder_plant_mapping where check_meter_no=:meterNumber AND end_date>:endDate AND status=:status", nativeQuery = true)
    MeterFeederPlantMappingBean findByCheckMeterNoAndEndDateAndStatus(@Param("meterNumber") String meterNumber,@Param("endDate") LocalDate endDate,@Param("status")  String active);
    @Query(value = "select * from ecell.re_meter_feeder_plant_mapping where standby_meter_no=:meterNumber AND end_date>:endDate AND status=:status", nativeQuery = true)
    MeterFeederPlantMappingBean findByStandbyMeterNoAndEndDateAndStatus(@Param("meterNumber") String meterNumber,@Param("endDate") LocalDate endDate,@Param("status")  String active);
    @Query(value = "select distinct plant_code from ecell.re_meter_feeder_plant_mapping where developer_id=:developerId AND end_date>:endDate AND status=:status",nativeQuery = true)
    List<String> findDistinctPlantCodeByDeveloperIdAndEndDateAndStatus(@Param("developerId")  String developerId,@Param("endDate") LocalDate endDate,@Param("status")  String active);

    @Query(value = "select * from ecell.re_meter_feeder_plant_mapping where developer_id=:developerId And plant_code=:plantCode AND end_date>:endDate AND status=:status",nativeQuery = true)
    MeterFeederPlantMappingBean findByDeveloperIdAndPlantIdAndEndDateAndStatus(@Param("developerId") String developerID,@Param("plantCode") String plantCode,@Param("endDate") LocalDate endDate, @Param("status") String active);
}
