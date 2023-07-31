package in.co.mpwin.rebilling.repositories.developermaster;

import in.co.mpwin.rebilling.beans.developermaster.DeveloperMasterBean;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeveloperMasterRepo extends CrudRepository<DeveloperMasterBean,Long> {

    List<DeveloperMasterBean> findAllByStatus(String status);
    List<DeveloperMasterBean> findByLocationIdAndStatus(String locationId,String status);

    DeveloperMasterBean save(DeveloperMasterBean developerMasterBean);

    DeveloperMasterBean findByIdAndStatus(Long id, String status);


}
