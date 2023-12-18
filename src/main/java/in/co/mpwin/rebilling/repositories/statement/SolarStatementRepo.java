package in.co.mpwin.rebilling.repositories.statement;

import in.co.mpwin.rebilling.beans.statement.SolarStatementBean;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SolarStatementRepo extends CrudRepository<SolarStatementBean,Long> {
    List<SolarStatementBean> findAllByMeterNumberAndMonthYearAndStatus(String meterNumber, String monthYear, String status);
}
