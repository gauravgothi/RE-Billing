package in.co.mpwin.rebilling.repositories.machinemaster;

import in.co.mpwin.rebilling.beans.machinemaster.MachineMasterBean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MachineMasterRepo extends CrudRepository<MachineMasterBean,Long> {

    List<MachineMasterBean> findAllByStatus(String status);

    MachineMasterBean save(MachineMasterBean machineMasterBean);

    MachineMasterBean findByIdAndStatus(Long id, String status);

    List<MachineMasterBean> findByLocationIdAndStatus(String locationId, String status);

    MachineMasterBean findByMachineCodeAndStatus(String machineCode, String status);


    @Query(value = "select last_value from ecell.re_machine_master_id_seq",nativeQuery = true)
    public Long getMaxSequence();
    @Query(value = "SELECT * FROM ecell.re_machine_master WHERE machine_code IN (:machineCodes) AND status=:status",nativeQuery = true)
    List<MachineMasterBean> findAllMachineByMachineCodeList(@Param("machineCodes") List<String> machineCodes, String status);
    @Query(value = "SELECT * FROM ecell.re_machine_master WHERE machine_code NOT IN (:mappedMachineCode) AND status=:status ORDER BY id DESC",nativeQuery = true)
    List<MachineMasterBean> findAllMachineExcludingMappedMachineCodeList(@Param("mappedMachineCode")  List<String> mappedMachineCode,@Param("status") String status);

    @Query(value = "SELECT MAX(CAST(SPLIT_PART(machine_code,'C',2)AS INTEGER)) FROM ecell.re_machine_master WHERE machine_code like 'MNC%'",nativeQuery = true)
    Integer findMaxMachineCode();


    MachineMasterBean findByMachineNameIgnoreCaseAndStatus(String machineName, String active);
}
