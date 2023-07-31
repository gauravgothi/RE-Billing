package in.co.mpwin.rebilling.repositories.plantmaster;

import in.co.mpwin.rebilling.beans.plantmaster.PlantMasterBean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantMasterRepo extends CrudRepository<PlantMasterBean,Long> {

    List<PlantMasterBean> findAllByStatus(String status);

    PlantMasterBean save(PlantMasterBean plantMasterBean);

    PlantMasterBean findByIdAndStatus(Long id, String status);

    List<PlantMasterBean> findByLocationIdAndStatus(String locationId, String status);

    PlantMasterBean findByPlantCodeAndStatus(String plantCode, String status);


    @Query(value = "select last_value from re_plant_master_id_seq",nativeQuery = true)
    public Long getMaxSequence();



    //@Query(value = "select max(id) from re_plant_master",nativeQuery = true)
    //public Long getMaxId();


}
