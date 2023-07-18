package in.co.mpwin.rebilling.repositories.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterMeCtr;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MeterMeCtrRepo extends CrudRepository<MeterMeCtr,Long> {
    public List<MeterMeCtr> findAll();
}
