package in.co.mpwin.rebilling.repositories.feedermaster;

import in.co.mpwin.rebilling.beans.feedermaster.FeederMasterBean;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FeederMasterRepo extends CrudRepository<FeederMasterBean,Long> {

    List<FeederMasterBean> findAllByStatus(String status);

//    FeederMasterBean findByIdAndStatus(Long id,String Status);
//
//    FeederMasterBean findByFeederNumberAndStatus(String feederNumber, String status);
}
