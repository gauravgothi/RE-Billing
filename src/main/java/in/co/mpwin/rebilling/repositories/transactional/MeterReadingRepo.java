package in.co.mpwin.rebilling.repositories.transactional;

import in.co.mpwin.rebilling.beans.readingoperations.MeterReading;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MeterReadingRepo extends CrudRepository<MeterReading,Long> {

    MeterReading save(MeterReading meterReading);

    List<MeterReading> findAllByStatus(String status);

}
