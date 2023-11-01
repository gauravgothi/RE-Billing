package in.co.mpwin.rebilling.repositories.statement;

import in.co.mpwin.rebilling.beans.statement.SolarStatementBean;
import org.springframework.data.repository.CrudRepository;

public interface SolarStatementRepo extends CrudRepository<SolarStatementBean,Long> {
}
