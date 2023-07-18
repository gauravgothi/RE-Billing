package in.co.mpwin.rebilling.repositories.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterMake;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MeterMakeRepo extends CrudRepository<MeterMake,Long> {
    public List<MeterMake> findAll();
}
