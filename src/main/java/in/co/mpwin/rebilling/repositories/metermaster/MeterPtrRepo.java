package in.co.mpwin.rebilling.repositories.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterPtr;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MeterPtrRepo extends CrudRepository<MeterPtr, Long> {

    public List<MeterPtr> findAllByStatus(String status);
    Optional<MeterPtr> findById(Long id);

}
