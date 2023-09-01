package in.co.mpwin.rebilling.repositories.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterReplacementBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MeterReplacementRepo extends CrudRepository<MeterReplacementBean, Long> {
    List<MeterReplacementBean> findAllByStatus(String status);
}
