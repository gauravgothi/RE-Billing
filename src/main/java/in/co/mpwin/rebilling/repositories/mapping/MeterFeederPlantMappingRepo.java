package in.co.mpwin.rebilling.repositories.mapping;

import in.co.mpwin.rebilling.beans.mapping.MeterFeederPlantMappingBean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

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


    List<MeterFeederPlantMappingBean> findByDeveloperIdAndStatus(String di, String status);

    List<MeterFeederPlantMappingBean>findByFeederCodeAndStatus(String fcode, String status);

    List<MeterFeederPlantMappingBean> findByPlantCodeAndStatus(String plantCode, String status);

    List<MeterFeederPlantMappingBean> findByStatus(String status);

    @Query(value = "select distinct t.plant_code from ecell.re_meter_feeder_plant_mapping t where t.developer_id='1' and status='active' order by t.plant_code",nativeQuery = true)
    List<String> findDistinctPlantCodeByDeveloperIdAndStatus(String di, String status);

    List<MeterFeederPlantMappingBean> findAllByDeveloperIdAndStatusOrderByEndDateAsc(String di, String status);
}
