package in.co.mpwin.rebilling.repositories.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterCtr;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MeterCtrRepo extends CrudRepository<MeterCtr,Long> {

    public List<MeterCtr> findAllByStatus(String status);

//    public MeterCtr findByCapacity(String capacity);
}
