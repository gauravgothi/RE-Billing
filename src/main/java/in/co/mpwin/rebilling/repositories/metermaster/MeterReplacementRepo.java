package in.co.mpwin.rebilling.repositories.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterReplacementBean;
import in.co.mpwin.rebilling.dto.MeterReplacementDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeterReplacementRepo extends CrudRepository<MeterReplacementBean, Long> {
}
