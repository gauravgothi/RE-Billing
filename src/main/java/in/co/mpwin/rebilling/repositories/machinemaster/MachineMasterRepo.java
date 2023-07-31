package in.co.mpwin.rebilling.repositories.machinemaster;

import in.co.mpwin.rebilling.beans.machinemaster.MachineMasterBean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MachineMasterRepo extends CrudRepository<MachineMasterBean,Long> {

    List<MachineMasterBean> findAllByStatus(String status);

    MachineMasterBean save(MachineMasterBean machineMasterBean);

    MachineMasterBean findByIdAndStatus(Long id, String status);

    List<MachineMasterBean> findByLocationIdAndStatus(String locationId, String status);

    MachineMasterBean findByMachineCodeAndStatus(String machineCode, String status);


    @Query(value = "select last_value from re_machine_master_id_seq",nativeQuery = true)
    public Long getMaxSequence();

}
