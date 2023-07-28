package in.co.mpwin.rebilling.repositories.feedermaster;

import in.co.mpwin.rebilling.beans.feedermaster.FeederMasterBean;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FeederMasterRepo extends CrudRepository<FeederMasterBean,Long> {

    List<FeederMasterBean> findAllByStatus(String status);

    FeederMasterBean save(FeederMasterBean feederMasterBean);

    FeederMasterBean findByIdAndStatus(Long id,String status);

    FeederMasterBean findByFeederNumberAndStatus(String feederNumber,String status);

    //Boolean existsByFeederNumberAndStatus(String feederNumber, String status);
}
