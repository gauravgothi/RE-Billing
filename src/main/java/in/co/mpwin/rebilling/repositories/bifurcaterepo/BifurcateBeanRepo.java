package in.co.mpwin.rebilling.repositories.bifurcaterepo;

import in.co.mpwin.rebilling.beans.bifurcation.BifurcateBean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface BifurcateBeanRepo extends CrudRepository<BifurcateBean,Long> {

    @Query(value ="SELECT * FROM ecell.re_bifurcated_reading Where hmeterno=:hMeterNumber AND hreading_date=:hReadingDate AND status=:status ORDER BY id ASC",nativeQuery = true)
    List<BifurcateBean> findAllByHMeterNumberAndHReadingDateAndStatus(
            @Param("hMeterNumber") String hMeterNumber,@Param("hReadingDate") Date hReadingDate,@Param("status") String status);

}
