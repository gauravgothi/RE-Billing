package in.co.mpwin.rebilling.repositories.readingrepo;

import in.co.mpwin.rebilling.beans.readingbean.AmrValidatedReading;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AmrValidatedReadingRepo extends CrudRepository<AmrValidatedReading,Long> {

    AmrValidatedReading save(AmrValidatedReading amrValidatedReading);

    List<AmrValidatedReading> findAllByStatus(String status);
}
