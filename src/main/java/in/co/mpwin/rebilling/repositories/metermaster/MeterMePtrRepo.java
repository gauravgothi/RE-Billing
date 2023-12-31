package in.co.mpwin.rebilling.repositories.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterMePtr;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MeterMePtrRepo extends CrudRepository<MeterMePtr, Long> {

    public List<MeterMePtr> findAllByStatus(String status);
}
