package in.co.mpwin.rebilling.repositories.plantmaster;

import in.co.mpwin.rebilling.beans.plantmaster.PlantMasterBean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
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



    @Query(value = "select * from ecell.re_plant_master where plant_code in (:mappedPlants) AND status=:status", nativeQuery = true)
    List<PlantMasterBean> findByPlantCodeListAndStatus(@Param("mappedPlants") List<String> mappedPlants, @Param("status") String status);

    @Query(value = "SELECT MAX(CAST(SPLIT_PART(plant_code,'C',2)AS INTEGER)) FROM ecell.re_plant_master WHERE plant_code like 'PC%'",nativeQuery = true)
    Integer findMaxInvestorCode();

    PlantMasterBean findByPlantNameIgnoreCaseAndStatus(String plantName, String active);
    @Query(value = "select * from ecell.re_plant_master where plant_code not in (:mappedPlantCodes) AND status=:status ORDER BY id ASC ", nativeQuery = true)
    List<PlantMasterBean> findUnmappedPlants(@Param("mappedPlantCodes") List<String> mappedPlantCodes, @Param("status") String status);
}
