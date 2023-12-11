package in.co.mpwin.rebilling.repositories.metermaster;

import in.co.mpwin.rebilling.beans.metermaster.MeterReplacementBean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MeterReplacementRepo extends CrudRepository<MeterReplacementBean, Long> {
    List<MeterReplacementBean> findAllByStatus(String status);

    @Query(value = "SELECT old_meter_number AS meter FROM ecell.re_meter_replacement t1 WHERE TO_CHAR(replace_date, 'Mon-YYYY')=:monthYear AND new_meter_number=:meterNo "
            +" UNION "
            +" SELECT new_meter_number AS meter FROM ecell.re_meter_replacement t2 WHERE TO_CHAR(replace_date, 'Mon-YYYY')=:monthYear AND old_meter_number=:meterNo",
            nativeQuery = true)
    List<String> findOtherMetersByReplacedMeterNumber(@Param("meterNo") String meterNo,@Param("monthYear") String monthYear);
}
