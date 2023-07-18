package in.co.mpwin.rebilling.repositories.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterDmf;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MeterDmfRepo extends CrudRepository<MeterDmf,Long> {
    public List<MeterDmf> findAllByStatus(String status);
}
